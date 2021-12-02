FROM openjdk:8

MAINTAINER fimon_lu@163.com

ADD target/melon-gateway-0.0.1-SNAPSHOT.jar melon-gateway.jar

EXPOSE 88

ENV PORT=88
ENV IP=113.31.153.208
ENV PROFILES=prod

CMD java -jar melon-gateway.jar --spring.profiles.active=$PROFILES --spring.cloud.nacos.discovery.ip=$IP --spring.cloud.nacos.discovery.port=$PORT