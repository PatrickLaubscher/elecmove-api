# Deployment script

## Elec'move

Summary:

Application main functionality : booking recharge stations for electric cars
- Backend API: Java v21, SpringBoot v3.4.5, Gradle
- Database: mySQL or MariaDB
- Frontend: node.js V22.14.0, Angular v20

***
## Prerequisites

- Node.js and npm

Add the NodeSource repository
```
curl -fsSL https://deb.nodesource.com/setup_22.x | sudo -E bash -
```
Install node V22.14.0 & npm
```
sudo apt install nodejs npm
```

Verify the version node V22.14.0
```
node -v
npm -v
```
***

## Script file docker in bash

```
# =========================
# CONFIGURATION
# =========================
BACKEND_REPO="git@github.com:youruser/elecmove-backend.git"
FRONTEND_REPO="git@github.com:youruser/elecmove-frontend.git"
BRANCH="main"

WORKDIR_BACKEND="/opt/elecmove-backend-src"
WORKDIR_FRONTEND="/opt/elecmove-frontend-src"

BACKEND_IMAGE="elecmove-backend:latest"
FRONTEND_IMAGE="elecmove-frontend:latest"

DOMAIN="elecmove.example.com"

# =========================
# 1. CLONE OR UPDATE REPOS
# =========================
if [ ! -d "$WORKDIR_BACKEND/.git" ]; then
    git clone "$BACKEND_REPO" "$WORKDIR_BACKEND"
else
    cd "$WORKDIR_BACKEND"
    git fetch --all
    git reset --hard "origin/$BRANCH"
fi

if [ ! -d "$WORKDIR_FRONTEND/.git" ]; then
    git clone "$FRONTEND_REPO" "$WORKDIR_FRONTEND"
else
    cd "$WORKDIR_FRONTEND"
    git fetch --all
    git reset --hard "origin/$BRANCH"
fi

# =========================
# 2. BUILD DOCKER IMAGES
# =========================

echo "=== Building backend Docker image ==="
cd "$WORKDIR_BACKEND"
docker build -t "$BACKEND_IMAGE" .

echo "=== Building frontend Docker image ==="
cd "$WORKDIR_FRONTEND"
docker build -t "$FRONTEND_IMAGE" .

# =========================
# 3. STOP AND REMOVE OLD CONTAINERS
# =========================
docker rm -f elecmove-backend || true
docker rm -f elecmove-frontend || true

# =========================
# 4. RUN CONTAINERS
# =========================

# Backend Spring Boot
docker run -d --name elecmove-backend \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  "$BACKEND_IMAGE"

# Frontend Angular (served with Nginx inside Docker)
docker run -d --name elecmove-frontend \
  -p 80:80 \
  "$FRONTEND_IMAGE"

# =========================
# 5. OPTIONAL: Push images to Docker registry
# =========================
# docker tag "$BACKEND_IMAGE" youruser/elecmove-backend:latest
# docker push youruser/elecmove-backend:latest
# docker tag "$FRONTEND_IMAGE" youruser/elecmove-frontend:latest
# docker push youruser/elecmove-frontend:latest

# =========================
# 6. VERIFICATION
# =========================
echo "=== Verifying deployment ==="
sleep 5
curl -f http://localhost:8080/actuator/health || echo "Backend health check failed"
curl -f http://localhost/ || echo "Frontend not reachable"

echo "✅ Docker deployment finished successfully!"
```


***
## Script file classic deployment in bash

Create in deploy.js

set -e  # stop on first error
set -o pipefail

```
# =========================
# CONFIGURATION - EDIT HERE
# =========================
BACKEND_REPO="git@github.com:youruser/elecmove-backend.git"
FRONTEND_REPO="git@github.com:youruser/elecmove-frontend.git"
BRANCH="main"

WORKDIR_BACKEND="/opt/elecmove-backend-src"
WORKDIR_FRONTEND="/opt/elecmove-frontend-src"
APP_DIR="/opt/elecmove"
FRONTEND_WEBROOT="/var/www/html/elecmove"
DOMAIN="elecmove.example.com"
SERVICE_NAME="elecmove"
GRADLE_CMD="gradle"  # or ./gradlew if present
```

```
# =========================
# INSTALL ENVIRONMENT
# =========================
echo "=== Installing prerequisites ==="
apt update -y
apt install -y openjdk-21-jdk curl npm nodejs apache2 mariadb-server git

# Install Gradle via SDKMAN if needed
if ! command -v gradle &>/dev/null; then
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"
    sdk install gradle 9.1.0
fi

# Install Angular CLI
npm install -g @angular/cli@20.2.2

# Secure MariaDB
mysql_secure_installation || true

# Enable Apache modules
a2enmod rewrite proxy proxy_http headers
systemctl restart apache2
```

```
# =========================
# 1. BACKEND
# =========================
echo "=== Deploying backend ==="
if [ ! -d "$WORKDIR_BACKEND/.git" ]; then
    git clone "$BACKEND_REPO" "$WORKDIR_BACKEND"
    cd "$WORKDIR_BACKEND" && git checkout "$BRANCH"
else
    cd "$WORKDIR_BACKEND"
    git fetch --all
    git checkout "$BRANCH"
    git reset --hard "origin/$BRANCH"
fi

cd "$WORKDIR_BACKEND/backend"

# Run backend unit tests
echo "=== Running backend unit tests ==="
$GRADLE_CMD test

# Build JAR
echo "=== Building backend ==="
$GRADLE_CMD clean build

mkdir -p "$APP_DIR"
cp -f build/libs/*.jar "$APP_DIR/app.jar"
chown -R www-data:www-data "$APP_DIR"

# systemd service
SERVICE_FILE="/etc/systemd/system/${SERVICE_NAME}.service"
cat > /tmp/${SERVICE_NAME}.service <<EOF
[Unit]
Description=Elec'move Backend Service
After=network.target

[Service]
User=www-data
ExecStart=/usr/bin/java -jar ${APP_DIR}/app.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

mv /tmp/${SERVICE_NAME}.service "$SERVICE_FILE"
systemctl daemon-reload
systemctl enable "$SERVICE_NAME"
systemctl restart "$SERVICE_NAME"
```

```
# =========================
# 2. FRONTEND
# =========================
echo "=== Deploying frontend ==="
if [ ! -d "$WORKDIR_FRONTEND/.git" ]; then
    git clone "$FRONTEND_REPO" "$WORKDIR_FRONTEND"
    cd "$WORKDIR_FRONTEND" && git checkout "$BRANCH"
else
    cd "$WORKDIR_FRONTEND"
    git fetch --all
    git checkout "$BRANCH"
    git reset --hard "origin/$BRANCH"
fi

cd "$WORKDIR_FRONTEND"
npm ci

# Run frontend tests before build
echo "=== Running frontend unit tests ==="
npx ng test --watch=false --browsers=ChromeHeadless

# Build Angular production
echo "=== Building frontend ==="
npx -y @angular/cli@20.2.2 ng build --configuration production

# Copy dist to Apache webroot
mkdir -p "$FRONTEND_WEBROOT"
rsync -a --delete dist/*/ "$FRONTEND_WEBROOT"
chown -R www-data:www-data "$FRONTEND_WEBROOT"

# .htaccess for Angular routes
cat > /tmp/.htaccess <<EOF
<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . /index.html [L]
</IfModule>
EOF

mv /tmp/.htaccess "$FRONTEND_WEBROOT/.htaccess"
```

```
# =========================
# 3. Apache configuration
# =========================
echo "=== Configuring Apache ==="
cat > /tmp/elecmove.conf <<EOF
<VirtualHost *:80>
    ServerName ${DOMAIN}
    DocumentRoot ${FRONTEND_WEBROOT}

    <Directory ${FRONTEND_WEBROOT}>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>

    # Reverse proxy for backend API
    ProxyPreserveHost On
    ProxyPass /api http://localhost:8080/api
    ProxyPassReverse /api http://localhost:8080/api

    ErrorLog \${APACHE_LOG_DIR}/elecmove-error.log
    CustomLog \${APACHE_LOG_DIR}/elecmove-access.log combined
</VirtualHost>
EOF

mv /tmp/elecmove.conf /etc/apache2/sites-available/elecmove.conf
a2ensite elecmove.conf
apachectl configtest
systemctl reload apache2
```

```
# =========================
# 4. Verification
# =========================
echo "=== Verification ==="
curl -f http://localhost:8080/actuator/health || true
curl -f http://$DOMAIN/api/actuator/health || true

echo "✅ Deployment finished successfully!"
```

## Script file classic deployment in JS

Create in deploy.js

Start script :
```
sudo node deploy.js
```

```
const { execSync } = require("child_process");
const fs = require("fs");
const path = require("path");

function run(cmd) {
  console.log(`\n>>> ${cmd}`);
  execSync(cmd, { stdio: "inherit", shell: "/bin/bash" });
}

/* ===========================
   === CONFIG ===
   =========================== */
const BACKEND_REPO = 'git@github.com:PatrickLaubscher/elecmove-api.git';
const FRONTEND_REPO = 'git@github.com:PatrickLaubscher/elecmove.git';
const BRANCH = 'main';

const WORKDIR_BACKEND = '/opt/elecmove-backend-src';
const WORKDIR_FRONTEND = '/opt/elecmove-frontend-src';

const APP_DIR = '/opt/elecmove';
const FRONTEND_WEBROOT = '/var/www/html/elecmove';
const DOMAIN = 'elecmove.example.com';
const SERVICE_NAME = 'elecmove';
```

```
/* ===========================
   === INSTALL ENVIRONMENT ==
   =========================== */
console.log("\n=== Installing prerequisites ===");
run("sudo apt update -y");

// Java OpenJDK 21
if(!fs.existsSync(`${process.env.HOME}/.sdkman`)) {
    run('curl -s https://get.sdkman.io | bash"); 
}
run("bash -c 'source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 21.0.6-tem'");

// Gradle via SDKMAN (if not installed)
if (!fs.existsSync(`${process.env.HOME}/.sdkman`)) {
  run("curl -s https://get.sdkman.io | bash");
  run("source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle 9.1.0");
}

// Node.js 22.x
run("curl -fsSL https://deb.nodesource.com/setup_22.x | sudo -E bash -");
run("sudo apt install -y nodejs npm");

// Angular CLI
run("sudo npm install -g @angular/cli@20.2.2");

// Apache with required modules
run("sudo a2enmod rewrite proxy proxy_http headers proxy_html");
run("sudo systemctl restart apache2");
```

```
// === BACKEND ===
if (!fs.existsSync(path.join(WORKDIR_BACKEND, '.git'))) {
  run(`git clone ${BACKEND_REPO} ${WORKDIR_BACKEND}`);
  run(`cd ${WORKDIR_BACKEND} && git checkout ${BRANCH}`);
} else {
  run(`cd ${WORKDIR_BACKEND} && git fetch --all`);
  run(`cd ${WORKDIR_BACKEND} && git checkout ${BRANCH}`);
  run(`cd ${WORKDIR_BACKEND} && git reset --hard origin/${BRANCH}`);
}

const backendDir = path.join(WORKDIR_BACKEND, 'backend');

// Run unit tests
console.log("\n=== Running backend unit tests ===");
run(`cd ${backendDir} && ${gradleCmd} test`);

// build gradle & copy app.jar if tests pass
run(`cd ${backendDir} && gradle clean build`);
run(`cp -f ${backendDir}/build/libs/app.jar ${APP_DIR}/app.jar`);

```

```
// === FRONTEND ===
if (!fs.existsSync(path.join(WORKDIR_FRONTEND, '.git'))) {
  run(`git clone ${FRONTEND_REPO} ${WORKDIR_FRONTEND}`);
  run(`cd ${WORKDIR_FRONTEND} && git checkout ${BRANCH}`);
} else {
  run(`cd ${WORKDIR_FRONTEND} && git fetch --all`);
  run(`cd ${WORKDIR_FRONTEND} && git checkout ${BRANCH}`);
  run(`cd ${WORKDIR_FRONTEND} && git reset --hard origin/${BRANCH}`);
}

const frontendDir = WORKDIR_FRONTEND;
run(`cd ${frontendDir} && npm ci`);

// run unit tests
console.log("\n=== Running frontend unit tests ===");
run(`cd ${frontendDir} && npx ng test --watch=false --browsers=ChromeHeadless`);

// build Angular if tests pass
run(`cd ${frontendDir} && npm ci`);
run(`cd ${frontendDir} && npx -y @angular/cli@20.2.2 ng build --configuration production`);

// copy dist to Apache webroot
run(`rsync -a --delete ${frontendDir}/dist/*/ ${FRONTEND_WEBROOT}/`);
```
***

