切换部署环境后需要修改的地方：

1、webapp/static/head/js/hqzx-login.js
	/*===测试环境会计官网地址===*/
	var HOSTNAME = "http://hqjy.com:8097";
	/*===测试环境学习中心地址(会计)===*/
	var learningCenterUrl = "http://177.77.77.183:18088/learningCenter/web/home?SSOTOKEN=";
	
2、webapp/static/head/js/head.js
	var accountant_domain = "http://hqjy.com:8097";
	var learningCenter_domain = "http://177.77.77.183:18088";
	
3、webapp/static/head/js/hqjyhead.js
	var accountant_domain = "http://www.hqjy.com";
	var learningCenter_domain = "http://kuaiji.learning.hqjy.com";
	
4、pom.xml文件profiles下面配置了开发环境，测试环境，生产环境

5、webapp/static/head/js/createLoginBox.js
	var accountant_domain = "http://www.hqjy.com";
	
6、webapp/WEB-INF/page/commons/header.ftl
	题库测试url：http://tikuweb.ljtest.hqjy.com
	
7、webapp/static/head/js/foot.js
	测试环境地址：var HOSTNAME = "http://kjpcweb.ljtest.hqjy.com";

6、在打包命令见文件mvn-*-build.bat
---------------------------------------------------------
以下是头部和尾部对于的文件,列出方便修改文案
头部：
/accountant/src/main/webapp/WEB-INF/page/commons/header.ftl
/accountant/src/main/webapp/static/head/js/head.js
/accountant/src/main/webapp/static/head/js/hqjyhead.js

尾部：
/accountant/src/main/webapp/WEB-INF/page/commons/footer.ftl
/accountant/src/main/webapp/static/head/js/foot.js