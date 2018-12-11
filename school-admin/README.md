
 **技术选型：** 
核心框架：Spring Framework 4.2
安全框架：Apache Shiro 1.3
视图框架：Spring MVC 4.2
持久层框架：MyBatis 3.3
定时器：Quartz 2.2
数据库连接池：Druid 1.0
日志管理：SLF4J 1.7、Log4j
页面交互：Vue2.x


 **软件需求** 
JDK1.7+
MySQL5.5+
Tomcat7.0+
Maven3.0+



 **本地部署**
- 通过git下载源码
- 创建数据库renren-security，数据库编码为UTF-8
- 执行doc/db.sql文件，初始化数据
- 修改db.properties文件，更新MySQL账号和密码
- Eclipse、IDEA执行【clean package tomcat7:run】命令，即可运行项目
- 项目访问路径：http://localhost
- 非Maven方式启动，则默认访问路径为：http://localhost:8080/renren-security


建议使用阿里云的Maven仓库：
```
<mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>        
</mirror>
```