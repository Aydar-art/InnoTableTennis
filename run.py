#!/usr/bin/env python3
import subprocess, os, signal, sys, time

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

# Backend
log('Starting backend (Spring Boot + H2)...')
env = os.environ.copy()
env['JAVA_HOME'] = '/opt/homebrew/opt/openjdk@17'
with open(BACKEND_LOG, 'w') as f:
    procs.append(subprocess.Popen(
        ['./mvnw', 'spring-boot:run', '-Dspring-boot.run.profiles=h2'],
        cwd=BACKEND_DIR, env=env, stdout=f, stderr=subprocess.STDOUT
    ))

# Frontend
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
        r = subprocess.run(['curl', '-so', '/dev/null', '-w', '%{http_code}',
                            'http://localhost:8080/api/matches'],
                           capture_output=True, text=True, timeout=5)
        if r.stdout.strip() == '200':
            log(f'Backend ready (attempt {i+1})')
            break
    except: pass
    log(f'  backend not ready yet, retrying... ({i+1}/30)')
    time.sleep(2)
else:
    log('ERROR: Backend failed to start. Check /tmp/backend.log')

# Wait for frontend
log('Waiting for frontend on http://localhost:5173 ...')
for i in range(15):
    try:
        r = subprocess.run(['curl', '-so', '/dev/null', '-w', '%{http_code}',
                            'http://localhost:5173'],
                           capture_output=True, text=True, timeout=5)
        if r.stdout.strip() == '200':
            log(f'Frontend ready (attempt {i+1})')
            break
    except: pass
    log(f'  frontend not ready yet, retrying... ({i+1}/15)')
    time.sleep(2)
else:
    log('ERROR: Frontend failed to start. Check /tmp/frontend.log')

log('All services are up!')
print()
print('  Frontend : http://localhost:5173')
print('  Backend  : http://localhost:8080')
print('  Login    : admin / admin')
print('  Backend  logs: tail -f /tmp/backend.log')
print('  Frontend logs: tail -f /tmp/frontend.log')
print('  Ctrl+C to stop all services')
print()

for p in procs:
    p.wait()
