# Hazelcast Eureka Service Discovery

## 1. Description
This is a project created to showcase how to manage a Hazelcast cluster using the Eureka Server.

We will be using the following modules from the spring reactive stacks:
1. Eureka Server
The Eureka Server will be used for the by the hazelcast server and hazelcast client to register

2. Hazelcast Server Cluster
3. Hazelcast Client
The hazelcast client will have access to the same resources as the hazelcast server, just that it does not participate in cluster replication and node coordination.

## 2. Setup
Note: Hazelcast Jet jobs will not run in Eclipse IDE. It's a known problem.

1. Start the Eureka Server:
To run as a fat-jar (Java version required: [11](https://adoptopenjdk.net/)):
```
mvn clean package
cd target
java -jar eureka-hazelcast-sd-provider-0.0.1-SNAPSHOT.jar
```

2. Start the Hazelcast Server:
To run as a fat-jar (Java version required: [11](https://adoptopenjdk.net/)):
```
mvn clean package
cd target
java -jar hazelcast-server-cluster-0.0.1-SNAPSHOT.jar
```

Optionally, you may run multiple instances of this service on other unused ports to form a cluster:
```
java -jar hazelcast-server-cluster-0.0.1-SNAPSHOT.jar --server.port=XXXX
```

3. Start the Hazelcast Client:
To run as a fat-jar (Java version required: [11](https://adoptopenjdk.net/)):
```
mvn clean package
cd target
java -jar hazelcast-client-0.0.1-SNAPSHOT.jar
```

Optionally, you may run multiple instances of this service on other unused ports to form a cluster:
```
java -jar hazelcast-client-0.0.1-SNAPSHOT.jar --server.port=XXXX
```