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
        LOGGER.error(e.getMessage());
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
## 0.2.38.PRE - 2020/07/17
* 修改 CompareAndPersistenceDataUtil。
## 0.2.39.PRE - 2020/07/17
* 升级Pocket依赖。
## 0.2.40.PRE - 2020/07/17
* 可根据明细数据进行过滤。
## 0.2.41.PRE - 2020/07/28
* 升级Pocket依赖，支持批量SQL语句执行，可查询当前时间。
## 0.2.43.PRE - 2020/07/30
* 列表查询条件支持‘或’操作。
## 0.2.44.PRE - 2020/08/01
* 升级Pocket依赖，将执行异常的SQL写入error日志文件中，将执行效率低下的SQL写入warn日志文件中。
## 0.2.45.PRE - 2020/08/03
* 首选项添加静态构造方法。
## 0.2.48.PRE - 2020/08/05
* 添加存储公用数据的工具类。
## 0.2.49.PRE - 2020/08/06
* 添加获取所有人员的方法。
## 0.2.50.PRE - 2020/08/06
* 修复重启数据库后语句执行异常。
## 0.2.52.PRE - 2020/08/07
* Fix bug.
* 保存更新时从表信息对末次操作人和末次操作时间属性进行赋值。
## 0.2.53.PRE - 2020/08/07
* @Column @ManyToOne 添加属性 ignoreCompare 标识在更新数据比较实体获取脏数据时是否忽略该属性（默认不忽略）
* 在调用 saveWithTrack、forcibleSaveWithTrack、updateWithTrack 时继承自 AbstractWithOperatorEntity 的明细实体类会同步更新其末次操作人和末次操作时间
## 0.2.53.PRE - 2020/08/11
* 修改Group添加属性parentUuid
## 0.2.55.PRE -2020/08/12
* 支持多级菜单分组
## 0.2.59.PRE -2020/08/18
* @Column添加encryptModel参数标注持久化时选择的加密方式以及查询时选择的解密方式。
* 加密字段禁止使用PoEl表达式进行更新。
* 使用SQLQuery更新加密字段时需要使用`EncryptUtil`手动进行加密。
```java
@Column(businessName = "身份证号码", encryptMode = EncryptType.DES)
private String idCode;
```

## 0.2.61.PRE -2020/08/19
- fix(util)获取分工下的人员。

## 0.2.63.PRE -2020/08/20
- @Join 以相同方式关联同一张表的不同字段则在生成的sql中以and链接（解决重复链接同一张表的问题）。

## 0.2.64.PRE -2020/08/20
- Criteria 添加方法 specifyField 指定部分查询列。
```java
Criteria criteria = this.session.createCriteria(Order.class);
criteria.add(Restrictions.equ("code", "C-006"))
        .add(Sort.asc("code"))
        .specifyField("code", "price");
List<Order> orderList = criteria.list();
```

## 0.2.65.PRE -2020/08/21
- @Join 以相同方式关联同一张表的相同字段不在生成的sql中以and链接（解决0.2.63.PRE的遗留BUG）。

## 0.2.67.PRE -2020/08/24
- Fix：级联保存给明细设置末次操作人和末次操作时间时抛出空指针异常。

## 0.2.68.PRE -2020/08/25
- Pair 重写hasCode和equals方法。

## 0.2.69.PRE -2020/08/26
- PERF：添加`@OneToOne` 注解。
```java
public class Order {

    @Column
    private String typeUuid;
    @OneToOne(ownField = "typeUuid", relatedField = "uuid")
    private OrderType orderType;
}

public class OrderType {

    @Identify
    @Column
    private String uuid;
    @Column
    private String name;
} 
```

## 0.2.71.PRE -2020/08/26
- PREF loadPage 可查询指定列。
```java
loadPage(FilterView filterView, String... fieldNames)
```

## 0.2.76.PRE -2020/09/04
- PREF 同一个映射类中不可以存在相同的businessName。

## 0.3.0.PRE -2020/09/07
- FIX: 修复主键生成策略。

## 0.3.2.PRE -2020/09/09
- FIX: 加载分工人员数据。

## 0.3.3.PRE -2020/09/10
- FIX: 修复事务管理切面。

## 0.3.6.PRE -2020/09/10
- PERF: 重构数据库会话及事务管理功能，@Affair注解可以在应用层、业务层、数据持久层使用，如果出现事务嵌套只开启最外层事务。
> 注意：<br/>
>可在 @Affair、@Service、@Session注解中设置数据库会话名称（优先级从高到低）

#### 使用方式
```java
// 第一种：
@Controller(bundleId = "post_setting", name = "岗位管理")
public class PostSettingController {

    @Affairs(on = false, sessionName = "biscuits")
    @Action(actionId = "list_post", method = RequestMethod.POST)
    public Body listPost(@RequestBody FilterView filterView){
        // do something...
    }
}
// 第二种：
@Session(sessionName = "biscuits")
@Controller(bundleId = "post_setting", name = "岗位管理")
public class PostSettingController {

    @Action(actionId = "list_post", method = RequestMethod.POST)
    public Body listPost(@RequestBody FilterView filterView){
        // do something...
    }
}
// 第三种：
@Service(session = "biscuits")
public class PostServiceImpl extends AbstractService implements PostService {

    @Override
    @Affairs(on = false)
    public List<MenuNode> getGroupTree(String serviceId) {
        // do something...
    }

}
```

## 0.3.11.PRE -2020/09/11
- PERF: 优化主键生成策略 ServerTableStationDateGenerator

## 0.3.13.PRE -2020/09/11
- PERF: 打印异常栈信息。
- FIX: 在Affairs嵌套前提下，切面多次捕捉异常后多次关闭数据库会话异常问题。

## 0.3.17.PRE -2020/09/16
- 查询单条数据可使用limit
- 修复事务提交和回滚问题
- 添加原子更新方法`atomUpdateWithTrack`

## 0.3.28.PRE -2020/09/29
- `AbstractCommonRepository` 添加原子更新方法 `atomUpdateWithTrack`
- 获取不到 `session` 时打印日志明确错误原因

## 0.3.29.PRE -2020/09/30
- 添加系统异常和业务异常抽象类。
例（自定义业务异常类枚举）：
```java
public enum CollectBloodExceptionEnum {
    ILLEGAL_BAR_CODE("E200001", "请扫描正确的条码。"),
    STATE_OF_SWITCH_DONOR("E200003", "编辑状态，无法切换献血者信息，请点击取消后重新扫码。");
    private final String exceptionCode;
    private final String exceptionDescription;

    CollectBloodExceptionEnum(String exceptionCode, String exceptionDescription) {
        this.exceptionCode = exceptionCode;
        this.exceptionDescription = exceptionDescription;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

}
```
例（自定义业务异常类）：
```java
public class CollectBloodException extends BusinessException {
    private BusinessExceptionEnum businessExceptionEnum;

    public CollectBloodException(String message) {
        super(message);
    }

    @Override
    public String getExceptionCode() {
        if (businessExceptionEnum == null) {
            return "F100000";
        }
        return businessExceptionEnum.getExceptionCode();
    }
}
```
使用：
```java
throw new CollectBloodException(CollectBloodExceptionEnum.ILLEGAL_BAR_CODE);
```

## 0.3.30.PRE -2020/10/12
- fix: 原子更新未根据数据标识更新。

## 0.3.31.PRE -2020/10/14
- perf: 添加用户头像，完善错误提示。

## 0.3.34.PRE -2020/10/22
- perf: 使用 targetClient 区分功能点所属的客户端，一个接口可同事提供给多种客户端。

## 0.3.35.PRE -2020/10/24
- perf: 关联查询被标记加密的字段。

## 0.3.36.PRE -2020/10/29
- fix: 升级依赖修复字段被多次加密问题。

## 0.3.38.PRE -2020/10/30
- perf: 可在外部创建Filter。
- perf: 删除T_BUNDLE中无用字段SORT。

## 0.3.47.PRE -2020/11/05
- perf: FilterView根据一对一关联类中的字段进行过滤查寻。
- perf: 启动时验证【 @Controller 相同 BundleId 的 Name 必须一致、不同 BundleId 的 TargetClient+Name 必须不同】。

## 0.3.49.PRE -2020/11/07
- perf: 添加单号生成器 IdGenerator。

方法定义：
```java
/**
     * 生成单号
     *
     * @param sessionName   数据库会话名称
     * @param clazz         持久化映射类 类 类型
     * @param fieldName     单号字段
     * @param preCode       前缀
     * @param dateFormatter 时间格式
     * @param strongSerial  是否强连续（是：阻塞，否：非阻塞）
     * @return 单号
     */
String generate(String sessionName, Class<? extends AbstractEntity> clazz, String fieldName, String preCode, String dateFormatter, ResetRule resetRule, Boolean strongSerial);
```
使用：

1.弱连续（不保证单号的连续性，非阻塞）
```java
String code = IdGenerator.getInstance().generate("biscuits", Order.class, "code", "10011039", "yyMMdd", ResetRule.DAY, false);
```
2.强连续（保证单号的连续性，阻塞）
```java
IdGenerator idGenerator = IdGenerator.getInstance();
try{
    String preCode = "10011039";
    String code = idGenerator.generate("biscuits", Order.class, "code", preCode, "yyMMdd", ResetRule.DAY, true);
    // 执行业务代码
    // ...
    idGenerator.commit(preCode);
} catch(Exception e){
    idGenerator.rollback(preCode);
}
```

## 0.3.52.PRE -2020/11/10
- feat: 添加单号生成工具IdGenerateUtil，根据 T_ID_RULE 的规则数据进行生成。

方法定义：
```java
    /**
     * 生成单号
     *
     * @param billType    单据类型 对应手工维护在T_BILL_TYPE表中的数据
     * @param clazz       类类型
     * @param fieldName   字段名
     * @param deptSpell   不萌首字母缩写
     * @param strongSn    是否强连续（如果值为 true 业务数据保存或报错后对应的调用单号生成工具的提交和回滚方法）
     * @param sessionName 数据库会话名
     * @return 单号
     * @throws Exception e
     */
    String generate(String billType, Class<? extends AbstractEntity> clazz, String fieldName, String deptSpell, boolean strongSn, String sessionName);

    /**
     * 单号跟随业务数据的持久化事务的提交而提交
     *
     * @param billType    单据类型
     * @param deptSpell   部门首字母缩写
     * @param sessionName 数据库会话名
     */
    void commit(String billType, String deptSpell, String sessionName);

    /**
     * 单号跟随业务数据的持久化事务的回滚而回滚
     *
     * @param billType    单据类型
     * @param deptSpell   部门首字母缩写
     * @param sessionName 数据库会话名
     */
    void rollback(String billType, String deptSpell, String sessionName);
```

使用：
```java
idGenerateUtil.generate("check"/* 订单类型 */, ConsultBean.class/* 对应的类类型 */, "code"/* 对应的字段 */, "TCK"/* 科室首字母缩写 */, false/* 是否强连续 */, "ibc"/* 数据库会话名 */);
```

## 0.3.55.PRE -2020/11/11
- feat: 获取部门公用方法。
- feat: 获取指定分工和部门下的人员。

# 0.4.0.PRE - 2020/11/18
FEAT: 链接池添加保活线程。

添加配置项：

- availableInterval 维护链接池中链接可用性的时间间隔（每几【默认7个小时】个小时使用链接池中的链接去执行一个简单的查询操作） 
- miniInterval 维护连接池中的最小链接数（默认24个小时）

# 0.4.5.PRE - 2020/11/23
- feat: 添加获取首选项工具类 PreferencesUtil。

使用方式：
```java
private final PreferencesUtil preferencesUtil;
Preferences checkFullAspPreferences = preferencesUtil.loadPreferences("donor_check", "IsCheckFullToAPSInterval");
```

# 0.4.13.PRE - 2020/11/24
fix: 修复单号生成重复问题。

# 0.4.15.PRE - 2020/11/24
feat: 修改错误提示信息。

# 0.4.16.PRE - 2020/11/24
## PERF
- timeout 默认值设置为 2s，修复获取链接长时间阻塞问题。
## FIX
- 修复查询加密数据问题: Restrictions.or(Restrictions...) 根据加密字段进行查询找不到值。

# 0.4.29.PRE - 2020/12/10
## PERF
- 完善初始化方式
```java
@Configuration
public class ApplicationInitialization {

    @Value("${biscuits.withPersistence:true}")
    private boolean withPersistence;
    @Resource
    private BiscuitsConfig biscuitsConfig;

    @PostConstruct
    public void run() throws Exception {
        if (withPersistence) {
            biscuitsConfig.setDesKey("sward007").setSm4Key("sward18713839007").init();
        }
    }
}
```
