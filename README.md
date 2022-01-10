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
### 3.1 RUN BACKEND SERVICE IN KUBERNETES
For local use we need minikube and docker. 

After minikube runs in docker we need to push the backend image to dockerhub, the name of the image has to have your username in it's tag.

```bash
docker tag backend-app YourDockerID/backend-app

docker push YourDockerID/backend-app
```

After the image is in dockerhub we create and .yaml file using kubectl from cli

```bash
kubectl create deployment nameOfPod --image=YourDockerID/nameOfImage --dry-run -o=yaml
```

After running this command you will get a genereted portion of code. Copy it and create a deployment.yaml file in your service

After you create your deployment.yaml from your service type in cmd

```bash
kubectl apply -f deployment.yaml
```
This will create our pod.
Then use [kubectl get all] to see all the pods. When our pod has RUNNING status then use 

```bash
kubectl port-forward POD_NAME 8080:8080 for backend
kubectl port-forward POD_NAME 3000:3000 for frontend
```