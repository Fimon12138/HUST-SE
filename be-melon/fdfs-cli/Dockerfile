FROM openjdk:8

MAINTAINER fimon_lu@163.com

ADD target/fdfs-cli-0.0.1-SNAPSHOT.jar fdfs-cli.jar

EXPOSE 8080

ENV PORT=8080
ENV IP=113.31.153.208
ENV PROFILES=prod

CMD java -jar fdfs-cli.jar --spring.profiles.active=$PROFILES --spring.cloud.nacos.discovery.ip=$IP --spring.cloud.nacos.discovery.port=$PORT