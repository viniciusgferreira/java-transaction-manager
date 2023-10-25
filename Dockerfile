FROM openjdk:17

WORKDIR /app

ADD ./target/transaction-manager-0.0.1-SNAPSHOT.jar transaction-manager-1.0.0.jar

ENTRYPOINT [ "java", "-jar", "transaction-manager-1.0.0.jar" ]
