
call mvn clean package -U -P testRemote -Dmaven.test.skip=true

copy target\*.war %OPT_PATH%\myappbase\webapps\webapps