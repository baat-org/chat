FROM adoptopenjdk/openjdk15:alpine-jre

# Dockerfile author / maintainer
MAINTAINER Sachin Goyal <sachin.goyal.se@gmail.com>

VOLUME /opt/chat

ADD build/libs/chat*.jar /opt/chat/chat.jar

ENTRYPOINT ["java","-jar","/opt/chat/chat.jar"]
