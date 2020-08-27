<div align="center">

# [![Awesome](https://awesome.re/badge.svg)](https://awesome.re) [![Build Status](https://travis-ci.org/leyan95/biscuits.svg?branch=master)](https://travis-ci.org/leyan95/biscuits)  [![](https://jitpack.io/v/leyan95/biscuits.svg)](https://jitpack.io/#leyan95/biscuits) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/62f21c28da8c4ef5867cf591d205543a)](https://www.codacy.com/app/leyan95/biscuits?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=leyan95/biscuits&amp;utm_campaign=Badge_Grade)

<img width="150" src="https://leyan95.github.io/biscuits/_media/biscuits.svg" alt="logo of docsify-awesome repository">

</div>

## Contents
- [Quick Start](https://leyan95.github.io/biscuits/#/pages/get-start)
- [Elaborate Tutoria](https://leyan95.github.io/biscuits/#/pages/step-by-step)
- [Demo](https://github.com/HunterVillage/biscuits-demo)

## 功能点设计思路
### 分层结构
![](https://github.com/wechat-program/album/blob/master/pic/cons/D2.png?raw=true)

#### 展现层
负责向用户展现信息以及解释用户命令。更细的方面来讲就是：

- 请求应用层以获取用户所需要展现的数据；
- 发送命令给应用层要求其执行某个用户命令；
    
#### 应用层
很薄的一层，定义软件要完成的所有任务。对外为展现层提供各种应用功能（包括查询或命令），对内调用领域层（领域对象或领域服务）完成各种业务逻辑，应用层不包含业务逻辑。

#### 领域层
负责表达业务概念，业务状态信息以及业务规则，领域模型处于这一层，是业务软件的**核心**。

#### 基础设施层
本层为其他层提供通用的技术能力；提供了层间的通信；为领域层实现持久化机制；总之，基础设施层可以通过架构和框架来支持其他层的技术需求；

### 功能点分块结构
![](https://github.com/wechat-program/album/blob/master/pic/cons/D3.png?raw=true)

#### 聚合
通过定义对象之间清晰的所属关系和边界来实现领域模型的内聚，并避免了错综复杂的难以维护的对象关系网的形成。聚合定义了一组具有内聚关系的相关对象的集合，我们把聚合看作是一个修改数据的单元。

#### 仓储
- 仓储被设计出来的目的是基于这个原因：领域模型中的对象自从被创建出来后不会一直留在内存中活动的，当它不活动时会被持久化到数据库中，然后当需 要的时候我们会重建该对象；重建对象就是根据数据库中已存储的对象的状态重新创建对象的过程；所以，可见重建对象是一个和数据库打交道的过程。从更广义的 角度来理解，我们经常会像集合一样从某个类似集合的地方根据某个条件获取一个或一些对象，往集合中添加对象或移除对象。也就是说，我们需要提供一种机制， 可以提供类似集合的接口来帮助我们管理对象。仓储就是基于这样的思想被设计出来的；
- 仓储里面存放的对象一定是聚合，原因是之前提到的领域模型中是以聚合的概念去划分边界的；聚合是我们更新对象的一个边界，事实上我们把整个聚合看 成是一个整体概念，要么一起被取出来，要么一起被删除。我们永远不会单独对某个聚合内的子对象进行单独查询或做更新操作。因此，我们只对聚合设计仓储。
- 尽管仓储可以像集合一样在内存中管理对象，但是仓储一般不负责事务处理。一般事务处理会交给一个叫“工作单元（Unit Of Work）”的东西。关于工作单元的详细信息我在下面的讨论中会讲到。
- 仓储在这里仅仅对自身的聚合进行**简单查询**和存储更新操作，如果涉及到了复杂查询（涉及多个功能点的关联查询）目前给出的解决方案是在基础设施层（Infrastructure）创建对应的查询服务（引被应用层`controller`直接调用故此处需自行开启事务，并使用SQLQuery执行对应的SQL）。

---

# 快速开始
## 应用功能点接口

- 类添加注解 `@Controller` ，其中 `bundleId` 为功能点的标识， `name` 为功能点的名称， `auth` 表示访问该功能点是否需要进行鉴权（默认需要）
- 接口方法添加注解 `@Action`，其中`actionId` 指定路径，`method` 指定请求方法，  `authId` 表示访问该接口所需要的权限（如果不设置值则不需要权限，如果 `@Controller` 中的 `auth` 为 `false` 则即使该接口设置了也不需要权限）

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

## 权限注册

说明：负责初始化权限，与 `@Action` 中的 `authId` 对应。

- 继承抽象类 `AbstractPermission`，添加注解 `@Component` 。
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

## 业务执行类
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

## 持久化类
### 接口

- 建议接口中的抽象方法上方添加清楚的文档说明
- 父类 `CommonRepository` 中已经定义了通用的方法如：增删改查等。

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

### 实现

- 该实现类需继承 `AbstractCommonRepository` 抽象类，同时实现上述自定义的接口。
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

## License
[MIT](https://choosealicense.com/licenses/mit/)
