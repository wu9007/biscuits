# *Skeleton*
这是一个需配合 [Pocket](https://github.com/leyan95/pocket) 使用的 `Spring` 小模板

## 使用方式
### Gradle 依赖
```groovy
dependencies {
    implementation('org.hunter:skeleton:0.0.1-PRE')
}
```

### 持久化
说明：与是数据库进行交互，负责对数据库的直接操作，如：保存，更新，查询

#### 接口
- 该接口必须继承 `Repository` 接口，位于 `org.hunter.skeleton.repository` 包中
- 建议接口中的抽象方法上方添加清楚的注释文档
```java
public interface UserRepository extends Repository {

    /**
     * save user.
     *
     * @param user user.
     * @return effect row number.
     */
    int saveUser(User user);
}
```

#### 实现
- 该实现类需继承 `AbstractRepository` 抽象类，同样位于 `org.hunter.skeleton.repository` 包中，同时实现上述自定义的接口
- 该类需添加 `@Repository` 注解，该注解位于 `org.springframework.stereotype` 包中
- 直接调用父类的 `getSession()` 方法获取 `session` 对象，对数据进行持久化操作
```java
@Repository
public class UserRepositoryImpl extends AbstractRepository implements UserRepository {

    @Override
    public int saveUser(User user) {
        return this.getSession().save(user);
    }
}
```

### 逻辑执行
说明：通过注入持多个久化类以不同的方式协同对业务的处理，避免使用 `session` 直接与是数据库进行交互
> 需定义在 `org.hunter.*.*.service` 包或其子包中

#### 接口
- 建议接口中的抽象方法上添加清楚的注释文档
```java
public interface PathService {

    /**
     * Load accessible paths by comma-separated role ID.
     *
     * @param roleIds role ids.
     * @return all accessible path.
     */
    String loadAccessiblePathByRoleIds(String roleIds);
}
```

#### 实现
- 该类需继承 `AbstractService` 抽象类，位于 `org.hunter.skeleton.service` 包中，同时实现上述自定义大的接口
- 该类需添加 `@Service` 注解，该注解位于 `org.hunter.skeleton.annotation` 包中，必须制定参数 `session` 的值，用来指定 `session name` 该名称与配置文件中的 `pocket.node.session` 中的某个值对应
- 在构造方法中使用 `@Autowired` 以及调用父类的 `injectRepository(Repository... repositories)` 方法注入所需的持久化类
- 该类中的方法可添加 `@Affairs` 注解指定是否开启事务，添加该注解事务默认开启，可通过 `on` 参数控制，若不添加该注解即表明无需开启事务
```java
@Service(session = "skeleton")
public class PathServiceImpl extends AbstractService implements PathService {
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final MapperRepository mapperRepository;

    @Autowired
    public PathServiceImpl(AuthorityRepository authorityRepository, RoleRepository roleRepository, MapperRepository mapperRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.mapperRepository = mapperRepository;
        this.injectRepository(this.authorityRepository, this.roleRepository, this.mapperRepository);
    }

    @Override
    @Affairs(on = false)
    public String loadAccessiblePathByRoleIds(String roleIds) {
        return Arrays.stream(roleIds.split(","))
                .map(this.roleRepository::findById)
                .filter(Objects::nonNull)
                .map(role -> role.getAuths(this.authorityRepository))
                .flatMap(Collection::parallelStream)
                .map(authority -> authority.getMappers(this.mapperRepository))
                .flatMap(Collection::parallelStream)
                .map(Mapper::getPath)
                .collect(Collectors.joining(","));
    }
}
```

### bundle注册
说明：负责在启动时将 `Bundle` （该类负责将路由进行捆绑）初始化，以便后续使用
- 继承 `org.hunter.skeleton.bundle` 包中的抽象类 `AbstractBundle`
- 添加注解 `@Component`
```java
@Component
public class ProvideBundle extends AbstractBundle {

    @Override
    public void init() {
        this.register("login", "服务可用性验证");
        this.register("bundle_setting", "菜单设置");
        this.register("bundle", "菜单分组");
    }
}
```

### 权限注册
说明：负责初始化权限，以便后续是使用
- 继承 `org.hunter.skeleton.permission` 包中的抽象类 `AbstractPermission`
- 添加注解 `@Component`
```java
@Component
public class ProvidePermission extends AbstractPermission {

    @Override
    public void init() {
        this.register("auth_bundle_read", "菜单查看权限", "这里写描述");
        this.register("auth_bundle_menage", "菜单管理权限", "这里写描述");
    }
}
```

## 请求处理
### 接口
- 建议接口中的抽象方法上添加清楚的注释文档
```java
public interface BundleController {

    /**
     * 获取所有的菜单分组及其所包含的菜单。
     *
     * @return body.
     */
    Body groupWithBundles();
}
```
### 实现
- 该类需继承 `AbstractController` 抽象类，位于 `org.hunter.skeleton.controller` 包中，同时实现上述自定义大的接口
- 类添加注解 `@Controller`，该注解位于 `org.hunter.skeleton.annotation` 包中，
必须制定参数 `bundleId` 程序启动时注册的 `bundleId` 相对应同样也对应访问根路径（如下方例子请求路径 `/bundle/grroup_bundles` ），参数 `auth` 指定在访问该类中的请求处理方法是是否需要相应的权限 默认 `true`
- 方法添加注解 `@Action`，同样位于 `org.hunter.skeleton.annotation` 包中，参数 `actionId` 指定路径，参数 `method` 指定请求方法
- 若请求某方法需要拥有相应权限，需要在方法上添加注解 `@Auth` 同样位于 `org.hunter.skeleton.annotation` 包，参数 `value` 指定需要哪一个权限，与初始化时注册的权限 `ID` 相对应
```java
@Controller(bundleId = "bundle")
public class BundleControllerImpl extends AbstractController implements BundleController {

    private final
    MenuService menuService;

    @Autowired
    public BundleControllerImpl(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    @Action(actionId = "/group_bundles", method = RequestMethod.GET)
    @Auth("auth_bundle_read")
    public Body groupWithBundles() {
        List<GroupView> groupViews = this.menuService.loadGroupWithBundles()
                .stream()
                .map(GroupView::new)
                .collect(Collectors.toList());
        return Body.newSuccessInstance("成功", "", groupViews);
    }
}
```
