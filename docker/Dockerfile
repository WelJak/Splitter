#FROM alpine:3.14
FROM openjdk:11
USER root
RUN mkdir /root/project
WORKDIR /root/project
COPY build/libs/Splitter-0.0.1-SNAPSHOT.jar /root/project/app.jar
EXPOSE 8088
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}","-jar","app.jar"]