## 应用功能点接口


- 类添加注解 `@Controller`，其中 `bundleId`为功能点的标识， `name`为功能点的名称， `auth`表示访问该功能点是否需要进行鉴权（默认需要）
- 接口方法添加注解 `@Action`，其中`actionId` 指定路径，`method` 指定请求方法，  `authId`表示访问该接口所需要的权限（如果不设置值则不需要权限，如果 `@Controller`中的 `auth`为 `false`则即使该接口设置了也不需要权限）



```java
@Controller(bundleId = "test_menu", name = "测试功能点")
public class TestMenuController {

    private final AuthService authService;

    public BasicController(AuthService authService) {
        this.authService = authService;
    }

    @Action(actionId = "group_bundle")
    @ApiOperation(value = "获取分组后的菜单", notes = "根据服务ID和用户名进行过滤")
    public Body getGroupBundle(@RequestParam String serviceId, @RequestHeader(name = "User-Avatar") String avatar, @RequestHeader("Business-Department-Uuid") String businessDepartmentUuid) throws SQLException {
        return Body.success().data(this.authService.getBundleGroup(serviceId, avatar, businessDepartmentUuid));
    }
}
```
<a name="tli9X"></a>
## 
<a name="HvTfa"></a>
## 权限注册

<br />说明：负责初始化权限，与 `@Action`中的 `authId`对应。

- 继承抽象类 `AbstractPermission`，添加注解 `@Component`。
```java
@Component
public class ProvidePermission extends AbstractPermission {

    @Override
    public void init() {
        this.register("test_read", "测试功能点查看权限", "描述...");
        this.register("test_menage", "测试功能点管理权限", "描述...");
    }
}
```
<a name="beb6a116"></a>
## 
<a name="QueNw"></a>
## 业务执行类
<a name="54ea89b4-1"></a>
### 接口


- 建议接口中的抽象方法上添加清楚的文档说明



```java
public interface AuthService {

    /**
     * 分组获取 BUNDLE
     *
     * @param serviceId              服务ID
     * @param avatar                 用户登陆名
     * @param businessDepartmentUuid 当前工作部门
     * @return 功能点分组
     * @throws SQLException e
     */
    List<Group> getBundleGroup(String serviceId, String avatar, String businessDepartmentUuid) throws SQLException;
}
```


<a name="38164c8b-1"></a>
### 实现


- 该类需继承 `AbstractService` 抽象类，同时实现上述自定义的接口
- 该类需添加 `@Service` 注解，指定 `session` 用以在执行方法时与指定数据库建立会话（该名称与配置文件中的 `pocket.node.session` 中的某个值对应）
- 该类中的方法可添加 `@Affairs` 注解指定是否开启事务，添加该注解事务默认开启，可通过 `on` 参数控制，若不添加该注解即表明不开启事务



```java
@Service(session = "authentication")
public class AuthServiceImpl extends AbstractService implements AuthService {
    private final BundleRepository bundleRepository;

    public AuthWorkUnitImpl(BundleRepository bundleRepository) {
        this.bundleRepository = bundleRepository;
    }

    @Override
    public List<Group> getBundleGroup(String serviceId, String avatar, String businessDepartmentUuid) throws SQLException {
        return this.bundleRepository.loadGroupBundleByServiceIdAndAvatarAndBusinessDepartmentUuid(serviceId.toUpperCase(), avatar, businessDepartmentUuid);
    }
}
```


<a name="zDEKO"></a>
## 持久化类
<a name="54ea89b4"></a>
### 接口


- 建议接口中的抽象方法上方添加清楚的文档说明
- 父类 `CommonRepository`中已经定义了通用的方法如：增删改查等。



```java
public interface BundleRepository extends CommonRepository<Bundle> {

    /**
     * 分组加载 用户可访问的 功能点
     * 如果 avatar 为 null 则不根据 avatar 过滤
     * 如果 businessDepartmentUuid 为 null 则不根据 businessDepartmentUuid 过滤
     *
     * @param serviceId              服务编号
     * @param avatar                 用户登陆名
     * @param businessDepartmentUuid 当前工作部门
     * @return 分组后的 BUNDLE
     * @throws SQLException e
     */
    List<Group> loadGroupBundleByServiceIdAndAvatarAndBusinessDepartmentUuid(String serviceId, String avatar, String businessDepartmentUuid) throws SQLException;
}
```


<a name="38164c8b"></a>
### 实现


- 该实现类需继承 `AbstractCommonRepository`抽象类，同时实现上述自定义的接口。
- 该类需添加 `@Component` 注解。
- 直接调用父类的 `getSession()` 方法获取 `session` 对象，对数据进行持久化操作。



```java
@Component
public class BundlePersistenceImpl extends AbstractCommonRepository<Bundle> implements BundlePersistencePort {

    @Override
    public List<Group> loadGroupBundleByServiceIdAndAvatarAndBusinessDepartmentUuid(String serviceId, String avatar, String businessDepartmentUuid) throws SQLException {
        Session session = this.getSession();
        // do load action...
    }
}

```


<a name="cAyBl"></a>
## 远程调用
```java
// 调用测试登录接口
public Body testLogin() throws Exception {
    Grab<Body> grab = Grab.newInstance("http://10.1.6.8/authentication/authentication/login");
    grab.setConnectTimeout(1000).setSocketTimeout(1000).setConnectionRequestTimeout(1000)
        .appendHeader("Content-Type", "application/json")
        .appendBody("userName", "admin")
        .appendBody("password", "123456");
    return grab.post(json -> {
        Body result;
        try {
            result = CommonObjectMapper.getInstance().readValue(json, Body.class);
        } catch (IOException e) {
            result = Body.error().title("失败").message("反序列化失败");
        }
        return result;
    });
}
```
