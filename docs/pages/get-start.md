# 🗺 Get Start
## 1.Install
> To create a `Spring-Boot` project, please check [link](https://spring.io/guides) for the detailed tutorial, which will not be repeated here.
#### Add it in your root `build.gradle`:
```groovy
repositories {
    mavenLocal()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
        implementation 'com.github.leyan95:biscuits:0.4.29.PRE'
        implementation 'com.github.leyan95:dipper:0.1.26.PRE'
}
```
#### If you use maven to manage project dependencies, Add it in your root `pom.xml`:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.leyan95</groupId>
    <artifactId>biscuits</artifactId>
    <version>0.4.29</version>
</dependency>

<dependency>
    <groupId>com.github.leyan95</groupId>
    <artifactId>dipper</artifactId>
    <version>0.1.26</version>
</dependency>
```
## 2.Edit profile
Add the following to your `application.yml`
#### Setup app name and port
```json
spring:
  application:
    name: demo
server:
  port: 8080
```
#### Connect to database:
```json
pocket:
  datasource:
    node:
      - url: jdbc:mysql://127.0.0.1:3306/demo?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&useAffectedRows=true
        nodeName: node-01
        driverName: com.mysql.cj.jdbc.Driver
        showSql: true
        user: root
        password: root
        poolMiniSize: 5
        poolMaxSize: 50
        session: biscuits
```
#### Add configuration information related to permissions:
```json
biscuits:
  token:
    secret: s-demo
    expiration: 600000
    refreshTime: 300000
    tokenHead: Bearer 
```

#### Init database
Run the SQL script [SQL](pages/bis.sql)
