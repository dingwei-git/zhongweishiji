FROM java:8-jre
MAINTAINER pxh@jovision.com
ENV envprofile=dev
ENV dbchange=mysql
VOLUME /tmp
COPY vi-login-1.0-SNAPSHOT.jar  login.jar
COPY /lib  /lib
EXPOSE 21000
ENTRYPOINT   ["java","-Xms512m","-Xmx512m","-Ddbtype=${dbchange}","-Dspring.profiles.active=${envprofile}", "-jar","login.jar"]

