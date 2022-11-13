FROM openjdk:17
ADD build/libs/spring-webflux.jar spring-webflux.jar
ENTRYPOINT ["java", "-jar", "spring-webflux.jar"]
EXPOSE 8080