## 0.2.0 - 2020/05/19
* 废弃抽象类 `AbstractController` ，所有API类无需继承此抽象类。
* 注解 `@Controller` 添加 `name` 属性，同时废弃 `AbstractBundle` 程序无需配置创建其子类来注册Bundle。
* 注解 `@Action` 添加 `authId` 属性指定访问该动作所需要的权限的标识，同时废弃 `@Auth` 。
* 废弃接口 `Repository` 持久化接口无需再继承此接口。
* 持久化类 `Mapper` 添加 `authId` 属性,表 `T_MAPPER` 添加 `AUTH_ID` 字段，同时废弃持久化类 `AuthMapperRelation` 及其对应的表 `T_AUTH_MAPPER` 。
---
* 可通过配置 `biscuits.withPersistence` 选择是否开启持久化（默认开启）。
* 增加控制层切面，如果存在请求头 `Trace-Id` `User-Name` 则切面记录日志到 `LogQueue` ， 可调用 `pollAccessorLog(int size)` 消费本地日志记录。
* 增加消费者切面，如果存在消息头同上。
        
> 注意：为了能够正确生成日志信息，
> 控制器务必使用注解 `org.hv.biscuits.annotation.Controller`, 
> 消费继承 `org.hv.biscuits.message.BisRocketMqListener` 或 带有相应消息的抽象类`org.hv.biscuits.message.BisRocketMqReplyListener`