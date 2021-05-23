# Spring Reactive Stack API

## 1. Description
This is a project created to showcase creating a backend service using the spring reactive stack.

We will be using the following modules from the spring reactive stacks:
1. spring-boot-starter-webflux
2. spring-boot-starter-data-r2dbc

For the user stories, we will refer to a randomly selected coding test instructions found online.\
See attached pdf: [JAVA-OfflineTest-April-2021.pdf](https://github.com/hr101191/playground/blob/master/e-wallet-api/JAVA-OfflineTest-April-2021.pdf)

## 2. Setup
To run as a fat-jar (Java version required: [11](https://adoptopenjdk.net/)):
```
mvn clean package
cd target
java -jar e-wallet-api-0.0.1-SNAPSHOT.jar
```

To run on a local docker container ([adoptopenjdk11](https://hub.docker.com/r/adoptopenjdk/openjdk11/) image is required):
```
docker build -t e-wallet-api .
docker run -d  -p 8080:8080 e-wallet-api:latest
```

## 3. API Documentations
API documentations is available at the following url after the project is running:
http://localhost:8080/swagger-ui.html