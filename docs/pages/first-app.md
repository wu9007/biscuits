# 🗺 Write Your First App
## 1.目录结构
```text
└── com
    └── example
        └── demo
            ├── controller
            ├── repository
            └── model
```

## 2.功能点实现
> 实现订单的 分页查询、新增、修改、详情、删除 功能
#### 在 model 目录下创建 `Order.java` 内容如下：
```java
@Entity(table = "TBL_ORDER", businessName = "订单")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1910123140032558152L;
    @Column
    private String code;
    @Column
    private LocalDateTime createTime;
    @Column
    private BigDecimal cost;
    // getter setter ...
}
```
#### 在 repository 创建接口 `OrderPersistencePort.java` 和 实现类`OrderPersistence.java`  
```java
public interface OrderPersistencePort extends CommonRepository<Order> {
}

@Component
public class OrderPersistence extends AbstractCommonRepository<Order> implements OrderPersistencePort {
}
```

#### 在 controller 目录下创建 `OrderController.java` 内容如下:
```java
@Controller(bundleId = "order", auth = true, name = "订单功能点")
public class OrderController {
    @Resource
    private OrderPersistencePort orderPersistence;
    // 访问 ./order/add 需要用户拥有 order_manager 权限
    @Action(actionId = "page_list", authId = "order_read", method = RequestMethod.POST)
    public Body pageList(@RequestBody FilterView filterView){
        return Body.success().data(orderPersistence.loadPage(filterView));
    }
    
    // 访问 ./order/add 需要用户拥有 order_manager 权限
    @Action(actionId = "add", authId = "order_manage", method = RequestMethod.POST)
    public Body add(@RequestBody Order order){
        return Body.success().data(orderPersistence.save(order));
    }
    
    // 访问 ./order/update 需要用户拥有 order_manager 权限
    @Action(actionId = "update", authId = "order_manage", method = RequestMethod.POST)
    public Body update(@RequestBody Order order){
        return Body.success().data(orderPersistence.update(order));
    }
    
    // 访问 ./order/delete 需要用户拥有 order_manager 权限
    @Action(actionId = "delete", authId = "order_manage", method = RequestMethod.POST)
    public Body delete(@RequestBody Order order){
        return Body.success().data(orderPersistence.delete(order));
    }
    
    // 访问 ./order/detail 需要用户拥有 order_manager 权限
    @Action(actionId = "detail", authId = "order_read")
    public Body detail(@RequstParam String uuid){
        return Body.success().data(orderPersistence.findOne(uuid));
    }
}
```
#### 在 demo 目录下创建 `ApplicationPermission.java` 内容如下：
```java
@Component
public class ApplicationPermission extends AbstractPermission {
    @Override
    public void init() {
        super.register("order_read", "查看", "订单查询权限");
        super.register("order_manage", "管理", "订单管理权限");
    }
}
```
