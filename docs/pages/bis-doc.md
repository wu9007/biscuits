# Biscuits

## 权限路由映射
### `@Controller` - 功能点【使用该注解的类中包含一组 API接口】
| 参数 | 值 |
| ---: | :--- |
| bundleId | 功能点 ID |
| auth | 访问该功能点下的接口是否需要指定的权限【默认 true】 |
| name | 功能点名称 |
| targetClient | 向指定客户端提供服务【默认 browser】 |

### `@Action` - API接口 
| 参数 | 值 |
| ---: | :--- |
| actionId | API 接口 ID |
| method | 请求方式 |
| authId | 指定访问该接口时需要的权限 |
| responseEncrypt | 响应体数据是否需要加密【默认 false】，加密方式：aes + rsa |
| consumes | 请求数据的格式 |
| produces | 响应数据的类型 |

```java
@Controller(bundleId = "card_grant", name = "证件发放", targetClient = {ClientEnum.PDA, ClientEnum.BROWSER})
public class CardGrantController {
    @Action(actionId = "page_list", method = RequestMethod.POST, authId = "card_grant_read")
    public Body pageList(@RequestBody FilterView filterView) throws SQLException {
        return Body.success().data(cardInfoService.listCardBean(filterView));
    }
}
```
> 该功能点是 PDA 和 BROWSER 共用的, <br/>
> 其中保研一个分页查询的接口：`./card_grant/page_list` <br/> 
> 用户需要拥有 card_grant_read 的权限才可以请求该接口 

## `@Affairs` 数据库会话
| 参数 | 值 |
| ---: | :--- |
| sessionName | 数据库会话名称 |
| on | 是否开启事务 【默认开启】 |

```java
@Affairs(sessionName = 'ibc', on = false)
public PageList<CardBean> listCardBean(FilterView filterView) throws SQLException {
    return cardGrantRepository.loadPageCascade(filterView);
}
```

## 保存历史数据 `@Track`
| 参数 | 值 |
| ---: | :--- |
| data | 存储对哪个实体操作的历史数据 |
| operator | 操作人 |
| operate | 操作类型 `OperateEnum` |
| operateName | 操作业务描述 |

```java
@Component
public class UserRepositoryImpl extends AbstractRepository implements UserRepository {
    @Override
    @Track(data = "#user", operator = "#avatar", operate = OperateEnum.SAVE, operateName = "#trackDescription")
    public int save(User user, String avatar, String trackDescription) {
      return this.getSession().save(user);
    }
}
```

- 新增内容
```json
{
  "业务": "保存标本离心单信息",
  "关键数据": {},
  "操作对象": "标本离心记录",
  "新增的数据": {
    "离心人": "00100180000120200704000001",
    "单据编号": "PCLXCXK1501",
    "是否删除": false,
    "离心数量": 0,
    "血站标识": "00100160000120200704000002",
    "设备简称": "---",
    "部门标识": "00100040000120200704000001",
    "离心程序标识": "1",
    "离心设备标识": "2",
    "标本离心程序名称": "核酸标本离心"
  }
}
```
- 编辑时所记录的数据
```json
{
  "业务": "更新标本离心单信息",
  "关键数据": {},
  "操作对象": "标本离心记录",
  "更新的数据": {
    "离心人": "由 00100180000120200704000001 改变为 00100180050120200811000002",
    "离心设备标识": "由 3 改变为 1"
  }
}
```
- 删除时所记录的数据
```json
{
  "业务": "删除标本离心单信息",
  "关键数据": {},
  "操作对象": "标本离心记录",
  "删除的数据": {
    "离心人": "00100180050120200811000002",
    "单据编号": "PCLXCXK1501",
    "是否删除": false,
    "离心数量": 0,
    "血站标识": "00100160000120200704000002",
    "设备简称": "---",
    "部门标识": "00100040000120200704000001",
    "离心程序标识": "1",
    "离心设备标识": "1",
    "标本离心程序名称": "核酸标本离心"
  }
}
```

## 报表数据源
### `@ReportDataInquirer` 数据源
| 参数 | 值 |
| ---: | :--- |
| id | 报表ID |
| name | 报表名称 |
| description | 报表描述 |
```java
@ReportDataInquirer(id = "demo-1", name = "<示例-1>", description = "简单报表查询示例")
public Map<String, Object> demoInquirer(FilterView filterView) throws InterruptedException {
    if (filterView != null && filterView.getFilters() != null) {
        // log query parameter
        for (Filter filter : filterView.getFilters()) {
            logger.info("key: {}, value: {}", filter.getKey(), filter.getValue());
        }
    }
    Map<String, Object> result = new LinkedHashMap<>(8);
    CountDownLatch countDownLatch = new CountDownLatch(2);
    executorService.execute(() -> {
        // query ...
        result.put("Table1", new ArrayList<>());
        countDownLatch.countDown();
    });
    executorService.execute(() -> {
        // query ...
        result.put("Table2", new ArrayList<>());
        countDownLatch.countDown();
    });
    countDownLatch.await();
    return result;
}
```
> 程序启动后会在数据库中生成一条数据，同时会将获取数据源的方法放入内存
### 报表样式
使用报表设计器将生成的样式文本保存到数据库中
### 报表打印
- 在客户端对报表进行分组授权。
- 携带报表ID和请求参数调用 `./report/print` 接口会调用指定报表ID的方法获取数据源，同时根据报表ID获取报表样式将构建的html输出给客户端。

## 单号规则
规则：平台+单据编码+科室+时间+流水号

例：PCLXCXK1501（PC端+离心+采血科+15日+01）

使用：
- 在客户端配置基本信息【单据类型，单据编码，是否区分部门，时间格式以及流水号位数和清零规则】
- 调用公共方法生成ID
```java
/**
 * 生成单号
 *
 * @param billType    单据类型 对应手工维护在T_BILL_TYPE表中的数据
 * @param clazz       类类型
 * @param fieldName   字段名
 * @param deptSpell   部门首字母缩写
 * @param strongSn    是否强连续（如果值为 true 业务数据保存或报错后对应的调用单号生成工具的提交和回滚方法）
 * @param sessionName 数据库会话名
 * @return 单号
 * @throws Exception e
 */
idGenerateUtil.generate("check", ConsultBean.class, "code", "TCK", false, "ibc");
```

## 操作链路追踪
- 可配置是否开启链路数据收集，是否收集数据前后镜像
- 数据存储在 MongoDB
### 应用层：
```json
[{
    "_id": {"$oid": "5ee371743f274752a0d9c7a5"},
    "_class": "com.shinowx.log.accessor.model.AccessorLog",
    "accessorName": "com.shinow.producert.adapters.api.ProductApi",
    "businessDeptName": "信息统计科",
    "endDateTime": {"$date": "2020-06-12T12:13:03.916Z"},
    "inParameter": "name: \"GXT1\"",
    "methodName": "rename",
    "outParameter": "\"rename\"",
    "requestId": "53c8cccbb1834d9ab63d5b792c32f382",
    "startDateTime": {"$date": "2020-06-12T12:13:03.862Z"},
    "traceId": "311564af8f3f4508ba8e641fe7115bd4",
    "userName": "管理员"
}]
```
### 业务层
```json
[{
     "_id": {"$oid": "5ee3716a3f274752a0d9c7a3"},
     "_class": "com.shinowx.log.accessor.model.ServiceLog",
     "endDateTime": {"$date": "2020-06-12T12:13:03.903Z"},
     "globalTransactionId": "6d69371df8fe47f4a86da77b8735956a",
     "inParameter": "uuid: \"1031024259\", name: \"GXT1\"",
     "methodName": "rename",
     "requestId": "53c8cccbb1834d9ab63d5b792c32f382",
     "serviceId": "58105413cfe14f0c949517a4f1472267",
     "serviceName": "com.shinow.producert.domain.worknuit.ProducerService",
     "startDateTime": {"$date": "2020-06-12T12:13:03.889Z"}
}]
```

### 数据持久层
```json
[{
     "_id": {"$oid": "5ee371603f274752a0d9c79f"},
     "_class": "com.shinowx.log.accessor.model.PersistenceLog",
     "endDateTime": {"$date": "2020-06-12T12:13:03.903Z"},
     "globalTransactionId": "6d69371df8fe47f4a86da77b8735956a",
     "inParameter": "uuid: \"1031024259\", name: \"GXT1\"",
     "methodName": "updateName",
     "persistenceId": "756d4707fbc84117905ad152014d818b",
     "persistenceName": "com.shinow.producert.adapters.persistence.ProductPersistence",
     "serviceId": "58105413cfe14f0c949517a4f1472267",
     "startDateTime": {"$date": "2020-06-12T12:13:03.889Z"}
}]
```
### 数据更新前后镜像
```json
[{
    "_id": {"$oid": "5ee371563f274752a0d9c79b"},
    "_class": "com.shinowx.log.accessor.model.OrmLog",
    "globalTransactionId": "6d69371df8fe47f4a86da77b8735956a",
    "persistenceId": "756d4707fbc84117905ad152014d818b",
    "persistenceMirrorView": {
      "sql": "UPDATE T_PRODUCT SET NAME = 'GXT1' WHERE T_PRODUCT.UUID = '1031024259'",
      "beforeMirror": [
        {
          "uuid": "1031024259",
          "name": "TXC",
          "since": "2014"
        }
      ],
      "afterMirror": [
        {
          "uuid": "1031024259",
          "name": "GXT1",
          "since": "2014"
        }
      ],
      "milliseconds": 6
    }
}]
```

## 公用方法
`AbstractCommonRepository` 包含新建、更新、删除、分页查询（可根据明细数据进行过滤）、查询所有、查询单条数据等方法，并可选择是否级联