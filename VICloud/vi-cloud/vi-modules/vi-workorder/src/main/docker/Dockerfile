FROM java:8-jre
MAINTAINER pxh@jovision.com
ENV envprofile=dev
VOLUME /tmp
COPY vi-workorder-1.0-SNAPSHOT.jar  workorder.jar
COPY /lib  /lib
RUN mkdir -p /home/vi-cloud/vi-workorder/images
EXPOSE 30000
ENTRYPOINT   ["java","-Xms512m","-Xmx512m","-Ddbtype=${dbchange}","-Dspring.profiles.active=${envprofile}", "-jar","workorder.jar"]

