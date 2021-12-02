FROM openjdk:8

MAINTAINER fimon_lu@163.com

ADD target/melon-activity-0.0.1-SNAPSHOT.jar melon-activity.jar

EXPOSE 8082

ENV PORT=8082
ENV IP=113.31.153.208
ENV PROFILES=prod

CMD java -jar melon-activity.jar --spring.profiles.active=$PROFILES --spring.cloud.nacos.discovery.ip=$IP --spring.cloud.nacos.discovery.port=$PORT