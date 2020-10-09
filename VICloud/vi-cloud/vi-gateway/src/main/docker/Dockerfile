FROM java:8-jre
MAINTAINER pxh@jovision.com
ENV envprofile=dev
VOLUME /tmp
COPY vi-gateway-1.0-SNAPSHOT.jar  gateway.jar
COPY /lib  /lib
EXPOSE 30000
ENTRYPOINT   ["java","-Xms512m","-Xmx512m","-Dspring.profiles.active=${envprofile}", "-jar","gateway.jar"]

