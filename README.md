# DEVOPS FROM 0 TO 100

This repository contains 2 services, one for backend and one for frontend.
Backend services runs on localhost on port 8080 and frontend on port 3000.

## 2. DOCKER
### 2.1 BUILD DOCKER CONTAINER BACKEND

```bash
mvn clean package

java -jar target/igrow-0.0.1-SNAPSHOT.jar (not mandatory, just to see if the app runs)

docker build . --tag=graphql-backend:latest

docker run -p 8080:8080 graphql-backend:latest
```
### 2.2 BUILD DOCKER CONTAINER FRONTEND
```bash
docker build . -t graphql-frontend:latest 

docker run -p 3000:3000 graphql-frontend:latest
```

### 2.3 USEFUL DOCKER COMMANDS
```bash
docker build [options] name -> creates a docker image from a Dockerfile with name choosed by you

docker run [options] dockerimagename  -> creates container from a docker image

docker ps -a  -> list all containers

docker start container_id -> starts container

docker stop container_id -> stops container

docker logs --follow container_id -> shows logs of that container in real time
```

## 3. KUBERNETES
