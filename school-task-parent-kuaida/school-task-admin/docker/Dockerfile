From 10.0.98.12:5000/jdk:8
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
VOLUME /tmp

VOLUME /logs

WORKDIR /home/service/school-task-admin

CMD ["java","-jar","school-task-admin.jar","--spring.profiles.active=dev"]

ADD ./target/school-task-admin.jar /home/service/school-task-admin/school-task-admin.jar