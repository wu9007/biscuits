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
>  该功能点是 PDA 和 BROWSER 共用的, <br/>
>  其中保研一个分页查询的接口：`./card_grant/page_list` <br/> 
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
### 报表样式
使用报表设计器将设计完成的