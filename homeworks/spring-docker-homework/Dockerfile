FROM openjdk:17
ADD target/library.jar library.jar
EXPOSE 8080
EXPOSE 27017
ENTRYPOINT ["java", "-jar", "library.jar"]