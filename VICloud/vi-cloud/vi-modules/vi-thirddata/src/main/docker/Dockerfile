FROM java:8-jre
MAINTAINER pxh@jovision.com
ENV envprofile=dev
ENV dbchange=mysql
VOLUME /tmp
COPY vi-thirddata-1.0-SNAPSHOT.jar  thirddata.jar
COPY /lib  /lib
EXPOSE 20001
ENTRYPOINT   ["java","-Xms512m","-Xmx512m","-Ddbtype=${dbchange}","-Dspring.profiles.active=${envprofile}", "-jar","thirddata.jar"]

