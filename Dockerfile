FROM openjdk:11

ADD ./build/libs/demo-0.0.1-SNAPSHOT.jar /
CMD ["java", "-jar", "-Djdk.tls.client.protocols=TLSv1.2", "/demo-0.0.1-SNAPSHOT.jar"]

