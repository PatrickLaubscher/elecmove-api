# Deployment procedure

## Elec'move

Summary:

Application main functionnality : booking recharge stations for electric cars
- Backend API: Java v21, SpringBoot v3.4.5, Gradle
- Database: mySQL or MariaDB
- Frontend: node.js V22.14.0, Angular v20

***
## Prerequisites

Check if you have the current dependencies installed :

- Java with OpenJDK 21.0.6 2025-01-21 LTS

For installing and updating
```
sudo apt update
sudo apt install openjdk21-jdk -y
```
Verify the version
```
java -version
```
- Gradle v9.1.0
```
sdk install gradle 9.1.0
```
Verify
```
gradle -v 
```

- Node.js and npm

Add the NodeSource respository
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

- Angular CLI v20.2.2
```
npm install -g @angular/cli 20.2.2
```
Verify the version 
```
ng v
```
- mySQL 
```
wget https://repo.mysql.com/mysql-apt-config_0.8.29-1_all.deb
sudo dpkg -i mysql-apt-config_0.8.29-1_all.deb
```
```
apt-cache search mysql-server
```
```
sudo apt-get install mysql-server
```

OR

- Maria DB
```
sudo apt-get install -y mariadb-server-10.5
```
Configuration
``` 
sudo mysql_secure_installation
```
***

## Compilation

### 1. Backend (Java Spring Boot with Gradle)
Build command 
```
cd backend
gradle clean build -x test
```
The compiled JAR will be generated there:
```
backend/build/libs/app.jar
```

### 2. Frontend (Angular)
Go to the frontend folder and install dependencies
```
cd frontend
npm install
```
Build for production:
```
ng build --configuration production
```
Build ouput will be there:
```
frontend/dist/
```

***
## Deployment 

### On Apache server

#### 1. Angular (frontend)

- copy front end file build output
```
/var/www/html/elecmove/
```

- edit VirtualHost configuration
```
/etc/apache2/sites-available/elecmove.conf
```
```
<VirtualHost *:80>
    ServerName elecmove.example.com
    DocumentRoot /var/www/html/elecmove

    <Directory /var/www/html/elecmove>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>

    ErrorLog ${APACHE_LOG_DIR}/elecmove-error.log
    CustomLog ${APACHE_LOG_DIR}/elecmove-access.log combined
</VirtualHost>
```

Confirm configuration:
```
sudo a2ensite elecmove.conf
sudo systemctl reload apache2
```

Add .htaccess on ```/var/www/html/elecmove```
```
<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . /index.html [L]
</IfModule>
```
Enable and restart server :
```
sudo a2enmod rewrite
sudo systemctl restart apache2
```

#### 2. Java Spring boot (backend)

- copy build file JAR to the server
```
/opt/elecmove/app.jar
```

- test with manual run
```
java -jar /opt/elecmove/app.jar --spring.profiles.active=prod &
```
- to stop the test
```
pkill -f 'app.jar'
```
- service file creation :
```
[Unit]
Description=Elec'move Backend Service
After=network.target

[Service]
User=www-data
ExecStart=/usr/bin/java -jar /opt/elecmove/app.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```
- reload server and lauch service:
```
sudo systemctl daemon-reload
sudo systemctl enable elecmove
sudo systemctl start elecmove
sudo systemctl status elecmove
```

#### 3. Reverse proxy to backend

- enable proxy config
```
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo systemctl restart apache2
```

- update /etc/apache2/sites-available/elecmove.conf
```
<VirtualHost *:80>
    ServerName elecmove.example.com
    DocumentRoot /var/www/html/elecmove

    <Directory /var/www/html/elecmove>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>

    # Reverse proxy for backend API
    ProxyPreserveHost On
    ProxyPass /api http://localhost:8080/api
    ProxyPassReverse /api http://localhost:8080/api

    ErrorLog ${APACHE_LOG_DIR}/elecmove-error.log
    CustomLog ${APACHE_LOG_DIR}/elecmove-access.log combined
</VirtualHost>
```

- reload Apache server:
```
sudo systemctl reload apache2
```
***

#### 4. Verification

- Check backend health endpoint:
```
curl http://localhost:8080/actuator/health
```
```
curl http://elecmove.example.com/api/actuator/health
```
