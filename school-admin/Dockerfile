FROM tomcat:9-jre8
MAINTAINER "async <asyncmessage@gmail.com>"
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
RUN mkdir -p "$CATALINA_HOME"
WORKDIR $CATALINA_HOME

# let "Tomcat Native" live somewhere isolated
ENV TOMCAT_NATIVE_LIBDIR $CATALINA_HOME/native-jni-lib
ENV LD_LIBRARY_PATH ${LD_LIBRARY_PATH:+$LD_LIBRARY_PATH:}$TOMCAT_NATIVE_LIBDIR

ADD ./docker/server.xml $CATALINA_HOME/conf/server.xml
WORKDIR $CATALINA_HOME/webapps
#RUN unzip school-admin.war
CMD ["catalina.sh","run"]"
ADD ./target/school-admin.war $CATALINA_HOME/webapps-war/