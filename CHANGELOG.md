## 0.1.19 - 2020/05/07
* 废弃抽象类`AbstractController`，所有API类无需继承此抽象类。
* 注解`@Controller`添加`name`属性，同时废弃`AbstractBundle`程序无需配置创建其子类来注册Bundle。
* 注解`@Action`添加`authId`属性指定访问该动作所需要的权限的标识，同时废弃`@Auth`。
* 废弃接口`Repository`持久化接口无需再继承此接口。
* 持久化类`Mapper`添加`authId`属性,表`T_MAPPER`添加`AUTH_ID`字段，同时废弃持久化类`AuthMapperRelation`及其对应的表`T_AUTH_MAPPER`。
