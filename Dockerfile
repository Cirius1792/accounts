FROM amazoncorretto:17.0.5-alpine
ARG API_KEY
ENV API_KEY=${API_KEY}
COPY ./target/accounts.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
