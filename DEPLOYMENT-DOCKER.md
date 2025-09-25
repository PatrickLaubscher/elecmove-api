# Deployment procedure

## Elec'move

Summary:

Application main functionnality : booking recharge stations for electric cars
- Backend API: Java v21, SpringBoot v3.4.5, Gradle
- Database: mySQL or MariaDB
- Frontend: node.js V22.14.0, Angular v20

***
## Prerequisites

Installation of Docker and Docker compose

```
sudo apt update
sudo apt install docker.io docker-compose -y
```


## Build docker image

### 1.Backend (Spring Boot + Java 21)

```
# Stage 1: build
FROM gradle:9.1.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Stage 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build command:
```
cd backend
docker build -t elecmove-backend:1.0 .
```

### 2. Frontend (Angular 20 + Node 22.14)

```
# Stage 1: build Angular app
FROM node:22.14 AS builder
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build -- --configuration production

# Stage 2: Nginx to serve files
FROM nginx:stable-alpine
COPY --from=builder /app/dist/ /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

Build command:
```
cd frontend
docker build -t elecmove-frontend:1.0
```

## Push docker images to Docker Hub

```
docker login
```

And then push
```
docker tag elecmove-backend:1.0 mydockeruser/elecmove-backend:1.0
docker tag elecmove-frontend:1.0 mydockeruser/elecmove-frontend:1.0

docker push mydockeruser/elecmove-backend:1.0
docker push mydockeruser/elecmove-frontend:1.0
```

Check repositories on Docker Hub 

## Deploy 

### 1. Pull images

```
docker pull mydockeruser/elecmove-backend:1.0
docker pull mydockeruser/elecmove-frontend:1.0
```

### 2. Create docker-compose.yml
```
version: "3.8"

services:
  backend:
    build: ./backend
    container_name: elecmove-backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/elecmove
      - SPRING_DATASOURCE_USERNAME=elecuser
      - SPRING_DATASOURCE_PASSWORD=elecpass
    depends_on:
      - db

  frontend:
    build: ./frontend
    container_name: elecmove-frontend
    ports:
      - "80:80"

  db:
    image: mysql:8.0
    container_name: elecmove-db
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: elecmove
      MYSQL_USER: elecuser
      MYSQL_PASSWORD: elecpass
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```

### 3. Start build

```
docker-compose up -d --build
```

### 4. Check on containers

```
docker ps
```