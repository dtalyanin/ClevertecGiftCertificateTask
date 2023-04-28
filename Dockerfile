FROM eclipse-temurin:17-jdk
COPY ./build/libs/*.jar ecl.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/ecl.jar"]