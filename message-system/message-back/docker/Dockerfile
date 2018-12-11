From 10.0.98.12:5000/jdk:8

VOLUME /tmp

VOLUME /logs

WORKDIR /home/service/msg

EXPOSE 8667

CMD ["java","-jar","-XX:+PrintGCDetails","-Xloggc:gc.log","msg-aync.jar","--spring.profiles.active=pro"]

ADD ./message-back/target/msg-aync.jar /home/service/msg/msg-aync.jar

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone