FROM openjdk:14
WORKDIR /spark-app
COPY ./target/app.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "--enable-preview", "app.jar"]