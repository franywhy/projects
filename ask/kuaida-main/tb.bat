
call mvn package -U -P testRemote -Dmaven.test.skip=true

copy target\*.war %opt_path%\myappbase\webapps\webapps