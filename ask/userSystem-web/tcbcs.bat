
call mvn clean package -P testRemote -Dmaven.test.skip=true
net stop jetty9
ping 127.0.0.1 -n 6
copy target\*.war %OPT_PATH%\myappbase\webapps\webapps
call %OPT_PATH%\clean_restart.bat