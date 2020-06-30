## 0.2.0.PRE - 2020/05/20
* 废弃抽象类 `AbstractController` ，所有API类无需继承此抽象类。
* 注解 `@Controller` 添加 `name` 属性，同时废弃 `AbstractBundle` 程序无需配置创建其子类来注册Bundle。
* 注解 `@Action` 添加 `authId` 属性指定访问该动作所需要的权限的标识，同时废弃 `@Auth` 。
* 废弃接口 `Repository` 持久化接口无需再继承此接口。
* 持久化类 `Mapper` 添加 `authId` 属性,表 `T_MAPPER` 添加 `AUTH_ID` 字段，同时废弃持久化类 `AuthMapperRelation` 及其对应的表 `T_AUTH_MAPPER` 。
---
* 可通过配置 `biscuits.withPersistence` 选择是否开启持久化（默认开启）。
* 增加控制层切面，如果存在请求头 `Trace-Id` `User-Name` 则切面记录日志到 `LogQueue` ， 可调用 `pollAccessorLog(int size)` 消费本地日志记录。
* 增加消费者切面，如果存在消息头同上。
* 配置文件中需要配置 RocketMq nameServer 地址。
        
> 注意：为了能够正确生成日志信息，
> 控制器务必使用注解 `org.hv.biscuits.annotation.Controller`, 
> 消费继承 `org.hv.biscuits.message.BisRocketMqListener` 或 带有响应消息的抽象类`org.hv.biscuits.message.BisRocketMqReplyListener`

## 0.2.1.PRE - 2020/05/22
* 增加工作单元日志切面。
* 增加 `@GlobalTransaction` 开启持久化操作前后镜像日志记录。
* 拆分权限与MQ相关模块。

## 0.2.2.PRE - 2020/06/12
* 存储客户端发送的rsa公钥。
```java
//例：
@RequestMapping(path = "/connect", method = RequestMethod.POST)
Body connect(@RequestParam String clientName, @RequestParam String publicKey) {
    try {
        ClientRsaUtil.addClientPublicKey(clientName, publicKey);
    } catch (Exception e) {
        e.printStackTrace();
        return Body.error().message(e.getMessage());
    }
    return Body.success().data(Base64.getEncoder().encodeToString(ServiceRsaUtil.getPublicKey().getEncoded()));
}

```
* 当`@Action`的 `responseEncrypt`设置为true时对响应体进行加密（rsa+aes）。
```java
// 加密响应体
@RestControllerAdvice
public class EncryptAdvice extends AbstractEncryptAdvice {
}
```
* 当请求头中的`encrypt`为true时，表示该请求携带的请求提是经过哭护短加密的。
```java
// 解密请求体
@RestControllerAdvice
public class DecryptAdvice extends AbstractDecryptAdvice {
}
```

* 加解密接口示例：
```java
@Action(actionId = "login", method = RequestMethod.POST, responseEncrypt = true)
public Body login(@RequestBody Map<String, String> userInfo) throws SQLException {
    String avatar = userInfo.get("userName");
    String password = userInfo.get("password");
    String departmentUuid = userInfo.get("departmentUuid");
    Session session = authorityCheck.login(avatar, EncodeUtil.abcEncoder(password), departmentUuid);
    return Body.success().message(String.format("%s 登录成功", session.getUserView().getName())).token(session.getToken()).data(session.getUserView());
}
```

## 0.2.3.PRE - 2020/06/15
* 用户表添加 职工编号：`STAFF_ID` 拼音码：`SPELL` 字段。 
* 规范属性命名 `serviceId`。

## 0.2.4.PRE - 2020/06/22
* 修改获取`serviceId`的方式，去除`application.name`尾部的数字。

## 0.2.5.PRE - 2020/06/22
* `bundle`可设置分组。

## 0.2.6.PRE - 2020/06/25
* 添加站和站级别实体类。

## 0.2.7.PRE - 2020/06/26
* 修改用户实体类CODE->AVATAR。

## 0.2.8.PRE - 2020/06/26
* 添加岗位实体类。
* 添加岗位人员关联实体类。

## 0.2.9.PRE - 2020/06/29
* 添加分工实体类。
* 添加岗位分工关联实体类。

## 0.2.10.PRE - 2020/06/30
* 部门实体类添加所属站标识和编码。
