From 10.0.98.12:5000/jdk:8

VOLUME /tmp

VOLUME /logs

WORKDIR /home/service/msg

EXPOSE 8666

CMD ["java","-jar","-XX:+PrintGCDetails","-Xloggc:gc.log","msg-web.jar","--spring.profiles.active=pro"]

ADD ./message-web/target/msg-web.jar /home/service/msg/msg-web.jar

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone