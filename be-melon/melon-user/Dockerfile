FROM openjdk:8

MAINTAINER fimon_lu@163.com

ADD target/melon-user-0.0.1-SNAPSHOT.jar melon-user.jar

ENV PORT=8081
ENV IP=113.31.153.208
ENV PROFILES=prod

EXPOSE $PORT

CMD java -jar melon-user.jar --spring.profiles.active=$PROFILES
    --spring.cloud.nacos.discovery.ip=$IP --spring.cloud.nacos.discovery.port=$PORT