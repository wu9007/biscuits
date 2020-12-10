## 多数据源配置

### 配置信息

```yaml
pocket:
  datasource:
    node:
      ## 数据源1
      - url: jdbc:mysql://127.0.0.1:3306/pocket1
        nodeName: mysql-01
        driverName: com.mysql.cj.jdbc.Driver
        ## 是否打印sql语句以及耗时
        showSql: true
        ## 仅打印执行时长超出1秒的语句
        warningLogTimeout: 1000
        ## 是否收集前后镜像
        collectLog: true
        user: root
        password: root
        poolMiniSize: 10
        poolMaxSize: 15
        timeout: 3
        session: session1,session2
      ## 数据源2
      - url: jdbc:oracle:thin:@127.0.0.1:1521:pocket2
        nodeName: oracle-01
        driverName: oracle.jdbc.driver.OracleDriver
        ## 加密后的用户名
        encryptedUser: PPBlj83b1dJ+5Z6+4XoE0w==
        ## 加密后的密码
        encryptedPassword: kV0+tnZsZqNtcrNZTpWECA==
        poolMiniSize: 10
        poolMaxSize: 15
        timeout: 3
        ##根据session找到数据库并从数据库对应的连接池中获取数据库链接，故所有session不可重复
        session: session3,session4
```


## 实体类规范


### 主类


```java
@Entity(table = "TBL_ORDER")
public class Order extends BaseEntity {
    private static final long serialVersionUID = 2560385391551524826L;

    // name 默认根据属性名驼峰转大写下划线
    // encryptModel 标注持久化时选择的加密方式以及查询时选择的解密方式
    @Column(businessName = "编号", encryptMode = EncryptType.DES)
    private String code;
    @Column
    private BigDecimal price;
    @Column
    private Date day;
    @Column
    private Date time;
    @Column
    private Boolean state;
    @Join(columnName = "TYPE", businessName = "订单支付方式", 
          joinTable = "TBL_ORDER_TYPE", joinMethod = JoinMethod.LEFT, 
          bridgeColumn = "UUID", destinationColumn = "NAME")
    private String type;
    @OneToOne(ownField = "typeUuid", relatedField = "uuid")
    private OrderType orderType;
    @OneToMany(clazz = Commodity.class, bridgeField = "order")
    private List<Commodity> commodities;
    @Column(ignoreCompare = true)
    private LocalDateTime lastOperationTime;

   // 这里省略 getter setter
}
```


### 一对一关联类
```java
@Entity(table = "TBL_ORDER_TYPE")
public class OrderType {

    @Identify
    @Column
    private String uuid;
    @Column
    private String name;
    
    // 这里省略 getter setter
}
```


### 一对多明细类


```java
@Entity(table = "TBL_COMMODITY", businessName = "订单明细")
public class Commodity extends BaseEntity {
   private static final long serialVersionUID = -6711578420837877371L;

   @Column
   private String name;
   @Column
   private BigDecimal price;

   @ManyToOne(columnName = "ORDER_UUID", clazz = Order.class, upBridgeField = "uuid")
   private Long order;

   // getter setter
}
```

## 数据操作


### 数据库会话对象


通过 SessionFactory 的静态方法`Session getSession(String sessionName)`获取对象。
```java
Session session = SessionFactory.getSession("session1");
```


### 使用 Session 进行数据操作


```java
// 开启事务
this.session = SessionFactory.getSession("session1");
this.session.open();
this.transaction = session.getTransaction();
this.transaction.begin();

// 查询
RelevantBill order = (RelevantBill) this.session.findOne(RelevantBill.class, "10130");
order.setCode("Hello-001");
//查询时间
LocalDateTime localDateTime = this.session.createSQLQuery().now()
// 更新
this.session.update(order);
// 删除
this.session.delete(order);
// 关闭事务
this.transaction.commit();
this.session.close();
```

### 使用 Criteria 查询数据

```java
Criteria criteria = this.session.createCriteria(Order.class);
// specifyField 查询指定列
criteria.specifyField("code", "price")
		.add(Restrictions.like("code", "A___"))
        .add(Restrictions.or(
            Restrictions.gt("price", 13), 
            Restrictions.lt("price", 12.58)
         ))
        .add(Sort.desc("price"))
        .add(Sort.asc("uuid"))
        .limit(0, 5);
List orderList = criteria.list();
```

### 使用 Criteria 更新数据

```java
Criteria criteria = this.session.createCriteria(Order.class);
criteria.add(Modern.set("price", 500.5D))
  		.add(Modern.set("day", new Date())
        .add(Restrictions.equ("code", "C-001")))
        .update()

// 为保证原子操作，已支持表达式更新，
// # 后面跟对应对象中的属性名，
// : 后对应参数展位符
session.createCriteria(Order.class)
        // 在原数据的基础上进行加操作
        .add(Modern.setWithPoEl("#price  = #price + :ADD_PRICE"))
        // 给 :ADD_PRICE 参数赋值
        .setParameter("ADD_PRICE", 100)
        .update();
```

### 使用 Criteria 根据条件删除数据

```java
Criteria criteria = session.createCriteria(Order.class);
criteria.add(Restrictions.equ("uuid", 1011011L)).delete();
```

### 使用 SQLQuery


```java
// 非持久化映射类
@View
public class OrderView implements Serializable {

    private static final long serialVersionUID = 2802482894392769141L;
    @Column
    private String code;
    @Column
    private BigDecimal price;
    // getter setter
}
```


```java
// 视图的使用
SQLQuery query = this.session.createSQLQuery("select CODE as code, PRICE as price from tbl_order where CODE = :ORDER_CODE AND DAY < :DAY", OrderView.class)
        .setParameter("ORDER_CODE", "C-001")
        .setParameter("DAY", new Date());
List<OrderView> orders = query.list();

// 查询单列
SQLQuery query = this.session.createSQLQuery("select uuid from tbl_order where CODE = :ORDER_CODE AND DAY < :DAY")
        .setParameter("ORDER_CODE", "C-001")
        .setParameter("DAY", new Date());
List<String> orders = query.list();

// mapperColumn的使用
List<String> types = Arrays.asList("006", "007", "008", "009");
SQLQuery query = this.session.createSQLQuery("select uuid, code from tbl_order where TYPE IN(:TYPE)")
        .mapperColumn("label", "value")
        .setParameter("TYPE", types);
List<Map<String, String>> orders = query.list();

// 批量语句执行
SQLQuery queryInsert = this.session.createSQLQuery("insert into tbl_order(uuid,code,price) values(:UUID, :CODE, :PRICE)");
for (int index = 0; index < 10; index++) {
    queryInsert.setParameter("UUID", "2020" + index)
            .setParameter("CODE", "C-00" + index)
            .setParameter("PRICE", index)
            .addBatch();
}
int[] rowInserts = queryInsert.executeBatch();
```


### 使用 ProcessQuery 调用存储过程查询数据


```java
ProcessQuery<Order> processQuery = session.createProcessQuery("{call test(?)}");
processQuery.setParameters(new String[]{"蚂蚁"});
Function<ResultSet, Order> mapperFunction = (resultSet) -> {
    try {
        Order order = new Order();
        order.setCode(resultSet.getString(1));
        return order;
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
};
Order order = processQuery.unique(mapperFunction);
```

### Session 发放说明

| **Modifier and Type** | **Method and Description** |  |
| :---: | --- | --- |
| <T extends AbstractEntity> T | [findOne](#hOAki)(Class<T> clazz, Serializable identify)
级联查询持久化对象 |  |
| <T extends AbstractEntity> T | [findOne](#iVLQ6)(Class<T> clazz, Serializable identify, boolean cascade)
查询持久化对象（可选是否级联查询） |  |
| <E extends AbstractEntity> List<E> | [list](#7uiUw)(Class<E> clazz)
级联查询持久化对象集合 |  |
| <E extends AbstractEntity> List<E> | [list](#oIazV)(Class<E> clazz, boolean cascade)_
查询持久化对象集合（可选是否级联查询） |  |
| int | [save](#u8Xzl)(AbstractEntity entity)
非级联保存持久化对象（NULL不纳入保存范围，保留数据库默认值） |  |
| int | [save](#4Awi0)(AbstractEntity entity, boolean cascade)
保存持久化对象（NULL不纳入保存范围，保留数据库默认值、可选是否级联保存） |  |
| int | [forcibleSave](#icJfO)(AbstractEntity entity)
非级联保存持久化对象（NULL同样也进行保存，不保留数据库默认值） |  |
| int | [forcibleSave](#bdPQ0)(AbstractEntity entity, boolean cascade)
保存持久化对象（NULL同样也进行保存，不保留数据库默认值、可选是否级联保存） |  |
| int | [update](#YGvhO)(AbstractEntity entity)
非级联更新持久化对象 |  |
| int | [update](#CqBFd)(AbstractEntity entity, boolean cascade)
更新持久化对象（可选是否级联更新） |  |
| int | [delete](#Vn7Ed)(AbstractEntity entity)
级联删除持久化对象 |  |
| int | [delete]()(AbstractEntity entity, boolean cascade)
删除持久化对象（可选是否级联删除） |  |

### Criteria 方法说明

| **Modifier and Type** | **Method and Description** |
| :---: | --- |
| <E extends AbstractEntity> List<E> | [list](.)()
非级联查询持久化对象集合 |
| <E extends AbstractEntity> List<E> | [listNotCleanRestrictions](.)()
非级联查询持久化对象集合（不清空条件） |
| <E extends AbstractEntity> List<E> | [list](.)(boolean cascade)
查询持久化对象集合（可选是否级联） |
| <E extends AbstractEntity> List<E> | [listNotCleanRestrictions](.)(boolean cascade)
查询持久化对象集合（不清空条件、可选是否级联） |
| <T extends AbstractEntity> T | [top](.)()
非查询_第一条数据_ |
| <T extends AbstractEntity> T | top(boolean cascade)
_获取第一条数据（可选是否级联）_ |
| Object | [max](.)(String field)
_查询最大值_ |
| long | [count](.)()
_查询总数_ |
| int | [delete](.)()
非级联删除数据 |
| <T extends AbstractEntity> T | [unique](.)()
非级联查询单条数据 |
| <T extends AbstractEntity> T | [unique](.)(boolean cascade)
查询单条数据（可选是否级联） |
| int | [update](.)()
非级联更新数据 |