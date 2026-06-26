#!/usr/bin/env python3
import subprocess, os, signal, sys, time, shutil

ROOT = os.path.dirname(os.path.abspath(__file__))
BACKEND_DIR = os.path.join(ROOT, 'TableTennisBackend')
FRONTEND_DIR = os.path.join(ROOT, 'frontend')
BACKEND_LOG = '/tmp/backend.log'
FRONTEND_LOG = '/tmp/frontend.log'
procs = []
IS_MAC = sys.platform == 'darwin'
IS_LINUX = sys.platform == 'linux'

def log(msg):
    print(f'[run.py] {msg}')
    sys.stdout.flush()

def cleanup(s, f):
    log('Shutting down...')
    for p in procs:
        p.terminate()
    log('Done')
    sys.exit(0)

def run(cmd, cwd=None, timeout=None, **kw):
    return subprocess.run(cmd, cwd=cwd, capture_output=True, text=True, timeout=timeout, **kw)

def confirm(question):
    ans = input(f'  ⚠ {question} [Y/n] ').strip().lower()
    return ans != 'n'

signal.signal(signal.SIGINT, cleanup)
signal.signal(signal.SIGTERM, cleanup)

# ── OS detection ────────────────────────────────────────────────

log(f'Detected OS: {"macOS" if IS_MAC else "Linux" if IS_LINUX else sys.platform}')

if not IS_MAC and not IS_LINUX:
    log('ERROR: Unsupported OS. This script works on macOS and Linux.')
    sys.exit(1)

# ── Install helpers ─────────────────────────────────────────────

def install_package(name, brew_name, apt_name, install_msg=''):
    if IS_MAC:
        if shutil.which('brew'):
            if confirm(f'{install_msg} Install {name} via Homebrew?'):
                log(f'  installing {name} (may require sudo)...')
                r = run(['brew', 'install', brew_name], timeout=300)
                if r.returncode == 0:
                    log(f'  ✓ {name} installed')
                    return True
                log(f'  ✗ failed to install {name}: {r.stderr[:200]}')
                return False
        else:
            log(f'  ✗ Homebrew not found. Install it first: https://brew.sh')
            return False
    elif IS_LINUX:
        if shutil.which('apt-get'):
            if confirm(f'{install_msg} Install {name} via apt? (requires sudo)'):
                log(f'  installing {name}...')
                r = run(['sudo', 'apt-get', 'install', '-y', apt_name], timeout=300)
                if r.returncode == 0:
                    log(f'  ✓ {name} installed')
                    return True
                log(f'  ✗ failed to install {name}: {r.stderr[:200]}')
                return False
        elif shutil.which('yum'):
            if confirm(f'{install_msg} Install {name} via yum? (requires sudo)'):
                r = run(['sudo', 'yum', 'install', '-y', apt_name], timeout=300)
                if r.returncode == 0:
                    log(f'  ✓ {name} installed')
                    return True
        else:
            log(f'  ✗ No package manager found (apt/yum)')
            return False
    return False


def install_node():
    if IS_MAC:
        return install_package('Node.js', 'node@18', 'nodejs',
                               'Node.js is required for the frontend.')
    elif IS_LINUX:
        if confirm('Install Node.js 18 via NodeSource?'):
            log('  adding NodeSource repository...')
            r = run(['/bin/bash', '-c',
                     'curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -'],
                    timeout=60)
            if r.returncode != 0:
                log(f'  ✗ failed to add NodeSource: {r.stderr[:200]}')
                return False
            r = run(['sudo', 'apt-get', 'install', '-y', 'nodejs'], timeout=120)
            if r.returncode == 0:
                log('  ✓ Node.js installed')
                return True
            log(f'  ✗ failed: {r.stderr[:200]}')
            return False
    return False

# ── Dependency check ────────────────────────────────────────────

log('Checking dependencies...')

# Python version
log(f'  ✓ Python {sys.version_info.major}.{sys.version_info.minor}')

# curl
if not shutil.which('curl'):
    install_package('curl', 'curl', 'curl', 'curl is required for health checks.')

# Java
java_home = None
java_ok = False
res = run(['java', '-version'], timeout=10)
ver_str = (res.stdout or '') + (res.stderr or '')
if any(f'version "{v}' in ver_str for v in ('17', '21', '25')):
    log('  ✓ Java found (compatible)')
    java_ok = True
elif shutil.which('java'):
    log('  ✓ Java found')
    java_ok = True
else:
    log('  ✗ Java not found')
    if IS_MAC:
        install_package('Java 17', 'openjdk@17', 'openjdk-17-jdk',
                         'Java 17 is required for the backend.')
    elif IS_LINUX:
        install_package('Java 17', 'openjdk@17', 'openjdk-17-jdk',
                         'Java 17 is required for the backend.')

# Find JAVA_HOME
for candidate in [
    '/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home',
    '/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home',
    '/usr/lib/jvm/java-17-openjdk-arm64',
    '/usr/lib/jvm/java-17-openjdk',
    os.environ.get('JAVA_HOME', ''),
]:
    if candidate and os.path.isfile(os.path.join(candidate, 'bin', 'java')):
        java_home = candidate
        break
if not java_home:
    if IS_MAC:
        res = run(['/usr/libexec/java_home', '-v', '17'], timeout=5)
        if res.returncode == 0:
            java_home = res.stdout.strip()
    java_home = java_home or shutil.which('java') or ''

if java_home:
    log(f'  ✓ JAVA_HOME = {java_home}')

# Node.js
node_ok = bool(shutil.which('node'))
npm_ok = bool(shutil.which('npm'))
if not node_ok:
    install_node()
    node_ok = bool(shutil.which('node'))
    npm_ok = bool(shutil.which('npm'))
if node_ok:
    v = run(['node', '-v']).stdout.strip()
    log(f'  ✓ Node.js {v}')
if npm_ok:
    log(f'  ✓ npm found')

# npm dependencies
if npm_ok:
    nm = os.path.join(FRONTEND_DIR, 'node_modules')
    if os.path.isdir(nm):
        log('  ✓ frontend dependencies installed')
    else:
        log('  installing frontend npm dependencies (this may take a while)...')
        log('  running: npm install')
        r = subprocess.run(['npm', 'install'], cwd=FRONTEND_DIR,
                          capture_output=False, timeout=None)
        if r.returncode == 0:
            log('  ✓ npm install complete')
        else:
            log(f'  ✗ npm install failed (exit code {r.returncode})')

# Maven wrapper
mvnw = os.path.join(BACKEND_DIR, 'mvnw')
if os.path.isfile(mvnw):
    log('  ✓ Maven wrapper found')
    if not os.access(mvnw, os.X_OK):
        os.chmod(mvnw, 0o755)
else:
    log('  ✗ mvnw not found at ' + mvnw)

# Pre-compile backend
if java_ok and java_home and os.path.isfile(mvnw):
    target = os.path.join(BACKEND_DIR, 'target', 'classes')
    if os.path.isdir(target) and os.listdir(target):
        log('  ✓ Backend already compiled')
    else:
        log('  compiling backend (first run, this may take a few minutes)...')
        env = os.environ.copy()
        full_java = os.path.join(java_home, 'bin', 'java') if os.path.isdir(java_home) else java_home
        if os.path.isfile(full_java):
            env['JAVA_HOME'] = os.path.dirname(os.path.dirname(full_java))
        r = subprocess.run([mvnw, 'compile', '-DskipTests'], cwd=BACKEND_DIR, env=env,
                          capture_output=False, timeout=None)
        if r.returncode == 0:
            log('  ✓ Backend compiled')
        else:
            log(f'  ⚠ compile issues (will retry at startup)')

log('All checks passed, starting services...\n')

# ── Start services ──────────────────────────────────────────────

env = os.environ.copy()
if java_home:
    full_java = os.path.join(java_home, 'bin', 'java') if os.path.isdir(java_home) else java_home
    jhome = os.path.dirname(os.path.dirname(full_java)) if os.path.isfile(full_java) else ''
    if jhome:
        env['JAVA_HOME'] = jhome

log('Starting backend (Spring Boot + H2)...')
with open(BACKEND_LOG, 'w') as f:
    procs.append(subprocess.Popen(
        [mvnw, 'spring-boot:run', '-Dspring-boot.run.profiles=h2'],
        cwd=BACKEND_DIR, env=env, stdout=f, stderr=subprocess.STDOUT
    ))

log('Starting frontend (SvelteKit + Vite)...')
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
    if i < 3 or (i + 1) % 5 == 0:
        log(f'  waiting... ({i+1}/30)')
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
    log(f'  waiting... ({i+1}/15)')
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
