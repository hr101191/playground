FROM adoptopenjdk/openjdk11
EXPOSE 8080
ADD target/e-wallet-api-0.0.1-SNAPSHOT.jar e-wallet-api.jar
ENTRYPOINT ["java", "-jar", "e-wallet-api.jar"]