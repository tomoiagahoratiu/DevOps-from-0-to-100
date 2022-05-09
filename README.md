# DEVOPS FROM 0 TO 100

Purpose of tutorial is to connect two services inside the kubernetes cluster.

If you try to run this on localhost replace "proxy": "http://backend-igrow:8080" from package json with "proxy": "http://localhost:8080"

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
You could have also initially put your docker id in the image name.

```bash
docker tag backend-igrow YourDockerID/backend-igrow

docker push YourDockerID/backend-igrow
```

After the image is in dockerhub we create and .yaml file inside the project which will be name deployment.yaml

The yaml file should look like this

```bash
apiVersion: apps/v1
kind: Deployment -> type of resource
metadata:
  name: backend-igrow -> name of deployment
  namespace: default -> in which namespace is created
spec:
  replicas: 1
  selector:
    matchLabels:
      component: java -> label used in the service .yaml
  template:
    metadata:
      labels:
        component: java
    spec:
      containers:
        - image: horatiut98/igrow-backend -> name of imaged used
          name: server
          ports:
            - containerPort: 8080 -> port on where our app runs
```

After you create your deployment.yaml from your service type in cmd

```bash
kubectl apply -f deployment.yaml
```
This will create our pod.
Then use [kubectl get deployments] to see all deployments.

By using [kubectl get pods] we can see that we also created one pod. When our pod has RUNNING status then use 

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

The yaml file should look like this (named servicek8s)

```bash
apiVersion: v1
kind: Service -> type of resource
metadata:
  name: backend-igrow
  namespace: default
spec:
  type: ClusterIP 
  selector:
    component: java -> label of deployment 
  ports:
    - port: 8080
      targetPort: 8080
```

To create service use: 
```bash
kubectl apply -f servicek8s.yaml
```

Use [kubectl get svc] to display your service.

Now we repeat the steps for our frontend deployment

```bash
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend-igrow
  namespace: default
spec:
  replicas: 1
  selector:
    matchLabels:
      component: web
  template:
    metadata:
      labels:
        component: web
    spec:
      containers:
      - image: horatiut98/graphql-frontend	
        name: client
        ports: 
          - containerPort: 3000
```

We also have to create a service for our frontend deployment. As you have seen before the backend service type is ClusterIP

ClusterIP -> forwards traffic inside the cluster but can't be accessed from the outside world. So we need to create a service of type LoadBalancer which will expose our service through the external world.

```bash
apiVersion: v1
kind: Service
metadata:
  name: igrow-frontend-service
  namespace: default
spec:
  type: LoadBalancer
  selector:
    component: web
  ports:
    - protocol: "TCP"
      port: 3000
      targetPort: 3000
```

Now you can execute [kubectl get svc] and see that on the EXTERNAL_IP of our frontend service we have 'pending'. By using [minikube tunnel] we create a network route on the host to the service of our cluster.

If we type again [kubectl get svc] we will see that we have an IP on EXTERNAL_IP. Copy the EXTERNAL_IP go trough your browser and type EXTERNAL_IP:3000, now the application should be available.

### 3.3 INGRESS-NGINX
https://github.com/kubernetes/ingress-nginx

Previous we did the networking using load balancer which is a legacy way of getting network traffic into a cluster. With LoadBalancer we can't access multiple services if we want to expose more of them.

In the production env for network traffic we use ingress-nginx.

With ingress we will route traffic based on some configuration. The ingress controller will take care that the routing configurations are met.


For installing ingress run 
```bash
minikube addons enable ingress
```

Now we need to configure ingress via ingress.yaml file.
```bash
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress-service
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  ingressClassName: nginx-example
  rules:
    - http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: igrow-frontend-service
                port:
                  number: 3000
```

From the .yaml file we can see that for path that start with "/" our traffic will be routed to the frontend service.

A new namespace "ingress-nginx" has been created in kubernetes (use [kubectl get ns] to see all namespaces). We will change namespaces using

```bash
kubectl config set-context --current --namespace=ingress-nginx
```
Now we also need to update all .yaml files at metadata -> namespace from default with ingress-nginx. Also we need to update the frontend service from LoadBalancer to ClusterIP because the ingress will take care of the networking. We can now apply all .yaml files.

Again we need to use [minikube tunnel] to create a network route on the host to the service of our cluster(ingress). The application is now avilalbe at 127.0.0.1





