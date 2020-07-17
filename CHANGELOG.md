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

## 0.2.11.PRE - 2020/06/30
* 添加首选项实体类。

## 0.2.12.PRE - 2020/07/01
* 添加注解生成策略 (服务标识+表标识+站标识+年月日+序列号)。
* 添加抽象映射基础类（包含公用字段 deleted lastOperator lastOperationDateTime）。
* 去掉 @Entity 的 tableId 属性，统一从表中获取。

## 0.2.13.PRE - 2020/07/02
* 添加级联列表查询。
* 修复查询tableId异常。

## 0.2.14.PRE - 2020/07/02
* 修改FilterView可人工添加过滤条件。
* 如果持久化类是AbstractWithOperatorEntity的子类在保存（saveWithTrack）和 更新（updateWithTrack）的时候自动为末次操作人和操作事件赋值。
* 删除无用的抽象类。

## 0.2.15.PRE - 2020/07/02
* 修复主键生成策略

## 0.2.16.PRE - 2020/07/02
FIX BUG

## 0.2.17.PRE - 2020/07/02
* upgrade filter view.

## 0.2.18.PRE - 2020/07/03
* 添加部门分类。

## 0.2.19.PRE - 2020/07/05
* 添加TreeNode树节点类。
* 添加出队所有日志数据的方法。

## 0.2.20.PRE - 2020/07/05
* 修改分工和岗位相关持久化映射实体类

## 0.2.21.PRE - 2020/07/06
* TreeNode 空属性不进行序列化

## 0.2.22.PRE - 2020/07/06
* 添加部门树和部门下拉查询工具
* 添加根据分工获取人员下拉查询工具

## 0.2.23.PRE - 2020/07/07
* 添加功能点和分组的关联表

## 0.2.24.PRE - 2020/07/07
* Station 添加是否是叶子节点字段

## 0.2.28.PRE - 2020/07/08
* 根据功能点和分工获取人员集合
* FIX BUG

## 0.2.30.PRE - 2020/07/08
* 修改Pair类

## 0.2.31.PRE - 2020/07/09
#### 支持两种持久化路由和权限基础数据的方式
* 本系统直接链接数据库进行保存
```java
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"org.hv.*"})
public class SettingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SettingApplication.class, args);
    }

    @Bean
    public ActionHolder getActionFactory() throws SQLException {
        ActionHolder actionFactory = ActionHolder.getInstance();
        actionFactory.setBiscuitsDatabaseSessionId("biscuits")
                .runWithDevelopEnvironment()
                .persistenceMapper(actionFactory.getOwnServiceId(), actionFactory.getActionMap())
                .persistencePermission(actionFactory.getOwnServiceId(), actionFactory.getPermissionMap());
        return actionFactory;
    }
}
```
* 从内存中取出后使用MQ发送给其他系统，委托给其他系统进行存储

```java
// 本系统生产消息
@Component
public class DxpActionHolderSender implements CommandLineRunner {

    @Resource
    private BisRocketMqTemplate bisRocketMqTemplate;

    @Override
    public void run(String... args) throws Exception {
        bisRocketMqTemplate.convertAndSend("Topic-Action-Holder-Persistence", ActionHolderView.newInstance(ActionHolder.getInstance()));
    }
}

// 其他系统消费消息
@Component
@RocketMQMessageListener(topic = "Topic-Action-Holder-Persistence", consumerGroup = "Topic-Action-Holder-Persistence-Group")
public class ActionHolderConsumer extends BisRocketMqListener<ActionHolderView> {
    private final Logger logger = LoggerFactory.getLogger(ActionHolderConsumer.class);

    @Override
    public void execute(ActionHolderView actionHolderView) throws Exception {
        logger.info("========================== 持久化服务 {} 的功能点（{}条） 、权限数据 （{}条） ==========================",
                actionHolderView.getServiceId(),
                actionHolderView.getBundleViewMap().size(),
                actionHolderView.getPermissionViewMap().size());
        ActionHolder.getInstance()
                .persistenceMapper(actionHolderView.getServiceId(), actionHolderView.getBundleViewMap())
                .persistencePermission(actionHolderView.getServiceId(), actionHolderView.getPermissionViewMap());
    }
}
```
## 0.2.32.PRE - 2020/07/10
* 获取人员拥有的岗位
## 0.2.33.PRE - 2020/07/10
* 未知属性仍可反序列化
## 0.2.34.PRE - 2020/07/10
* FIX BUG
## 0.2.35.PRE - 2020/07/10
* UPGRADE POCKET VERSION.
## 0.2.36.PRE - 2020/07/14
* UserView序列化调整。
## 0.2.37.PRE - 2020/07/17
* 修改 CompareAndPersistenceDataUtil。
