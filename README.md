# DEVOPS FROM 0 TO 100

This repository contains 2 services, one for backend and one for frontend.
Backend services runs on localhost on port 8080 and frontend on port 3000.

For completing this tutorial you need knowldege of docker and kubernetes. If you don't understand some terminology read the documentation.

https://docs.docker.com/

https://kubernetes.io/docs/home/

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
### 3.1 RUN SERVICE IN KUBERNETES
For local use we need minikube and docker. 

After minikube runs in docker we need to push the backend image to dockerhub, the name of the image has to have your username in it's tag.

```bash
docker tag backend-igrow YourDockerID/backend-igrow

docker push YourDockerID/backend-igrow
```

After the image is in dockerhub we create and .yaml file using kubectl from cli

```bash
kubectl create deployment backend-igrow --image=horatiut98/graphql-backend --dry-run -o=yaml
```

After running this command you will get a genereted portion of code. Copy it and create a deployment.yaml file in your service

The yaml file should look like this

```bash
apiVersion: apps/v1
kind: Deployment 
metadata:
  creationTimestamp: null
  labels:
    app: backend-igrow -> label of pod, we will need this later to create a service
  name: backend-igrow
spec:
  replicas: 1
  selector:
    matchLabels:
      app: backend-igrow
  strategy: {}
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: backend-igrow
    spec:
      containers:
        - image: horatiut98/graphql-backend -> where we find image (our dockerId) + image name = image from where we created the pod
          name: graphql-backend
          resources: {}
status: {}
```

After you create your deployment.yaml from your service type in cmd

```bash
kubectl apply -f deployment.yaml
```
This will create our pod.
Then use [kubectl get all] to see all the pods. When our pod has RUNNING status then use 

```bash
kubectl port-forward backend-igrow 8080:8080 for backend
```

By using this we forward it's port which is 8080 to localhost:8080 so we can access it.
Another example.

```bash
kubectl port-forward POD_NAME 5000:8080
```

Listen on port 5000 locally and targets 8080 in pod.

Now repeat the steps for the frontend service, use port-forward POD_NAME 3000:3000 for it.

### 3.2 COMMUNICATION BETWEEN TWO PODS

After we create both pods and we are sure that they are working in order for our pods to communicate with each other we need to create services.

```bash
kubectl create service clusterip igrow-backend-service --tcp 8080:8080 --dry-run -o=yaml
```

After running this command you will get a genereted portion of code. Copy it and create a servicek8s.yaml file in your service

The yaml file should look like this

```bash
apiVersion: v1
kind: Service
metadata:
  creationTimestamp: null
  labels:
    app: igrow-backend-service
  name: igrow-backend-service
spec:
  ports:
  - name: 8080-8080
    port: 8080
    protocol: TCP
    targetPort: 8080
  selector:
    app: igrow-backend-service
  type: ClusterIP
status:
  loadBalancer: {}
```
