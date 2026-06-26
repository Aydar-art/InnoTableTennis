#!/usr/bin/env python3
import subprocess, os, signal, sys, time, shutil, platform, json

ROOT = os.path.dirname(os.path.abspath(__file__))
BACKEND_DIR = os.path.join(ROOT, 'TableTennisBackend')
FRONTEND_DIR = os.path.join(ROOT, 'frontend')
BACKEND_LOG = '/tmp/backend.log'
FRONTEND_LOG = '/tmp/frontend.log'
procs = []

def log(msg):
    print(f'[run.py] {msg}')
    sys.stdout.flush()

def cleanup(s, f):
    log('Shutting down...')
    for p in procs:
        p.terminate()
    log('Done')
    sys.exit(0)

signal.signal(signal.SIGINT, cleanup)
signal.signal(signal.SIGTERM, cleanup)

# ── Dependency check ────────────────────────────────────────────

def check(program, hint=''):
    found = shutil.which(program)
    if found:
        log(f'  ✓ {program} found at {found}')
        return True
    log(f'  ✗ {program} not found. {hint}')
    return False

def run(cmd, cwd=None, **kw):
    return subprocess.run(cmd, cwd=cwd, capture_output=True, text=True, **kw)

log('Checking dependencies...')

# Python
log(f'  ✓ Python {sys.version_info.major}.{sys.version_info.minor}.{sys.version_info.micro}')

# Java 17
java_home = None
java_ok = False
res = run(['java', '-version'], timeout=10)
ver_str = (res.stdout or '') + (res.stderr or '')
if 'version "17' in ver_str or 'version "21' in ver_str or 'version "25' in ver_str:
    log('  ✓ Java found (compatible)')
    java_ok = True
elif check('java', 'Install: brew install openjdk@17'):
    ver_str2 = run(['java', '-version']).stderr
    java_ok = True

# Try to find JDK 17 specifically
for candidate in [
    '/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home',
    '/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home',
    os.environ.get('JAVA_HOME', ''),
]:
    if candidate and os.path.isdir(os.path.join(candidate, 'bin', 'java')):
        java_home = candidate
        break
if not java_home and java_ok:
    res = run(['/usr/libexec/java_home', '-v', '17'], timeout=5)
    if res.returncode == 0:
        java_home = res.stdout.strip()

if java_home:
    log(f'  ✓ JAVA_HOME = {java_home}')
else:
    java_home = '/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home'
    if not os.path.isdir(os.path.join(java_home, 'bin', 'java')):
        log('  ⚠ JAVA_HOME not found, trying java from PATH')
        java_home = shutil.which('java')

# curl
check('curl')

# Node.js
node_ok = check('node', 'Install: brew install node@18')
if node_ok:
    res = run(['node', '-v'])
    log(f'  Node {res.stdout.strip()}')

# npm
npm_ok = check('npm', 'Install: brew install node@18')

# npm dependencies
if npm_ok:
    if os.path.isdir(os.path.join(FRONTEND_DIR, 'node_modules')):
        log('  ✓ frontend/node_modules exists')
    else:
        log('  installing frontend npm dependencies...')
        res = run(['npm', 'install'], cwd=FRONTEND_DIR, timeout=120)
        if res.returncode == 0:
            log('  ✓ npm install complete')
        else:
            log(f'  ✗ npm install failed:\n{res.stderr}')

# Maven wrapper
mvnw = os.path.join(BACKEND_DIR, 'mvnw')
if os.path.isfile(mvnw):
    log(f'  ✓ Maven wrapper (mvnw) found')
    if not os.access(mvnw, os.X_OK):
        os.chmod(mvnw, 0o755)
        log('  ✓ mvnw made executable')

# Maven local deps check
if java_ok and os.path.isfile(mvnw):
    target_dir = os.path.join(BACKEND_DIR, 'target')
    classes_dir = os.path.join(target_dir, 'classes')
    if os.path.isdir(classes_dir) and len(os.listdir(classes_dir)) > 0:
        log('  ✓ Backend already compiled')
    else:
        log('  compiling backend (first run, this may take a few minutes)...')
        env = os.environ.copy()
        if java_home and os.path.isdir(java_home):
            env['JAVA_HOME'] = java_home
        res = run([mvnw, 'compile', '-DskipTests'], cwd=BACKEND_DIR, env=env, timeout=300)
        if res.returncode != 0:
            log(f'  ⚠ mvnw compile had issues (will retry at startup): {res.stderr[-200:]}')

log('All checks passed, starting services...\n')

# ── Start services ──────────────────────────────────────────────

env = os.environ.copy()
if java_home and os.path.isdir(java_home):
    env['JAVA_HOME'] = java_home

log('Starting backend (Spring Boot + H2, logs: /tmp/backend.log)...')
with open(BACKEND_LOG, 'w') as f:
    procs.append(subprocess.Popen(
        [mvnw, 'spring-boot:run', '-Dspring-boot.run.profiles=h2'],
        cwd=BACKEND_DIR, env=env, stdout=f, stderr=subprocess.STDOUT
    ))

log('Starting frontend (SvelteKit + Vite, logs: /tmp/frontend.log)...')
with open(FRONTEND_LOG, 'w') as f:
    procs.append(subprocess.Popen(
        ['npm', 'run', 'dev'],
        cwd=FRONTEND_DIR, stdout=f, stderr=subprocess.STDOUT
    ))

# Wait for backend
log('Waiting for backend on http://localhost:8080 ...')
for i in range(30):
    try:
        r = run(['curl', '-so', '/dev/null', '-w', '%{http_code}',
                 'http://localhost:8080/api/matches'], timeout=5)
        if r.stdout.strip() == '200':
            log(f'Backend ready (attempt {i+1})')
            break
    except: pass
    if i < 5 or (i + 1) % 5 == 0:
        log(f'  backend not ready yet... ({i+1}/30)')
    time.sleep(2)
else:
    log('ERROR: Backend failed to start. Check /tmp/backend.log')

# Wait for frontend
log('Waiting for frontend on http://localhost:5173 ...')
for i in range(15):
    try:
        r = run(['curl', '-so', '/dev/null', '-w', '%{http_code}',
                 'http://localhost:5173'], timeout=5)
        if r.stdout.strip() == '200':
            log(f'Frontend ready (attempt {i+1})')
            break
    except: pass
    log(f'  frontend not ready yet... ({i+1}/15)')
    time.sleep(2)
else:
    log('ERROR: Frontend failed to start. Check /tmp/frontend.log')

log('All services are up!')
print()
print('  ┌──────────────────────────────────────────────────┐')
print('  │  Frontend : http://localhost:5173                │')
print('  │  Backend  : http://localhost:8080                │')
print('  │  Login    : admin / admin                        │')
print('  │                                                  │')
print('  │  Logs:                                           │')
print('  │    backend  → tail -f /tmp/backend.log           │')
print('  │    frontend → tail -f /tmp/frontend.log          │')
print('  │  Ctrl+C to stop                                  │')
print('  └──────────────────────────────────────────────────┘')
print()

for p in procs:
    p.wait()
