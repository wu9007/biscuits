package org.hunter.skeleton.launcher;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionFactory;
import org.hunter.pocket.session.Transaction;
import org.hunter.skeleton.annotation.Action;
import org.hunter.skeleton.annotation.Auth;
import org.hunter.skeleton.annotation.Controller;
import org.hunter.skeleton.controller.AbstractController;
import org.hunter.skeleton.spine.model.AuthMapperRelation;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.permission.Permission;
import org.hunter.skeleton.permission.PermissionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/2
 */
@Component
@Order(1)
public class AuthLauncher implements CommandLineRunner {

    private final
    Map<String, AbstractController> controllerMap;

    private final
    List<Permission> permissionList;

    private final
    ApplicationContext context;

    private String serverName;

    @Autowired
    public AuthLauncher(Map<String, AbstractController> controllerMap, @Nullable List<Permission> permissionList, ApplicationContext context) {
        this.controllerMap = controllerMap.entrySet().stream()
                .filter(item -> {
                    Controller controller = item.getValue().getClass().getAnnotation(Controller.class);
                    return controller.auth();
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.context = context;
        this.permissionList = permissionList;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.permissionList != null) {
            this.serverName = Objects.requireNonNull(this.context.getEnvironment().getProperty("spring.application.name")).toUpperCase();

            Session session = SessionFactory.getSession("skeleton");
            session.open();
            Transaction transactional = session.getTransaction();
            transactional.begin();
            PermissionFactory.init(this.initPermissionMap(session));
            this.permissionList.forEach(Permission::init);
            Map<String, Mapper> oldMapperMap = this.getMapperMap(session);
            if (oldMapperMap != null && oldMapperMap.size() > 0) {
                this.deleteUselessMapper(session, oldMapperMap);
            }
            Map<String, AuthMapperRelation> oldAuthMapperIdMap = this.getAuthMapperIdMap(session);
            if (oldAuthMapperIdMap != null && oldAuthMapperIdMap.size() > 0) {
                this.deleteUselessAuthMapper(session, oldAuthMapperIdMap);
            }
            saveAuthAndMapper(session, oldMapperMap, oldAuthMapperIdMap);
            transactional.commit();
            session.close();
        }
    }

    /**
     * 拿到数据库中的已有权限
     *
     * @param session session
     * @return 权限集合
     * @throws Exception e
     */
    private Map<String, Authority> initPermissionMap(Session session) throws Exception {
        Criteria criteria = session.creatCriteria(Authority.class);
        criteria.add(Restrictions.equ("serverName", serverName));
        List<Authority> authorityList = criteria.list();
        return authorityList.stream().collect(Collectors.toMap(item -> item.getServerId() + "_" + item.getId(), item -> item));
    }

    /**
     * 从数据库中获取所有映射 Mapper
     *
     * @param session session
     * @return 所有映射 Mapper
     * @throws Exception e
     */
    private Map<String, Mapper> getMapperMap(Session session) throws Exception {
        Criteria mapperCriteria = session.creatCriteria(Mapper.class);
        mapperCriteria.add(Restrictions.equ("serverName", serverName));
        List<Mapper> mapperList = mapperCriteria.list();
        return mapperList.stream().collect(Collectors.toMap(item -> item.getBundleId() + item.getActionId(), item -> item));
    }

    /**
     * 获取 Map 键为：服务名称+权限id+路径id
     *
     * @param session session
     * @return 从数据库中获取所有 权限和路由关系
     * @throws Exception e
     */
    private Map<String, AuthMapperRelation> getAuthMapperIdMap(Session session) throws Exception {
        Criteria criteria = session.creatCriteria(AuthMapperRelation.class);
        criteria.add(Restrictions.equ("serverName", serverName));
        List<AuthMapperRelation> authMapperList = criteria.list();
        return authMapperList.stream()
                .collect(Collectors.toMap(item -> {
                    try {
                        Authority authority = (Authority) session.findOne(Authority.class, item.getAuthUuid());
                        Mapper mapper = (Mapper) session.findOne(Mapper.class, item.getMapperUuid());
                        return authority.getServerId() + "_" + authority.getId() + "_" + mapper.getBundleId() + mapper.getActionId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }, item -> item));
    }

    /**
     * 保存数据库中没有的 Mapper 和 Authority
     *
     * @param session            session
     * @param oldMapperMap       数据库中查旧的的 Mapper
     * @param oldAuthMapperIdMap 数据库中旧的 Authority
     */
    private void saveAuthAndMapper(Session session, Map<String, Mapper> oldMapperMap, Map<String, AuthMapperRelation> oldAuthMapperIdMap) {
        controllerMap.forEach((k, v) -> {
            String bundleId = v.getClass().getAnnotation(Controller.class).bundleId()[0];
            Method[] methods = v.getClass().getMethods();
            Arrays.stream(methods)
                    .filter(this.isMapperMethod)
                    .forEach(method -> {
                        Mapper mapper = this.getMethodMapper(bundleId, method);
                        String mapperId = mapper.getBundleId() + mapper.getActionId();
                        try {
                            Auth auth = method.getAnnotation(Auth.class);
                            String auhId = auth.value();
                            Authority authority = PermissionFactory.get(serverName, auhId);
                            if (authority != null) {
                                if (!oldMapperMap.containsKey(mapperId)) {
                                    session.save(mapper);
                                } else {
                                    mapper = oldMapperMap.get(mapperId);
                                }
                                AuthMapperRelation authMapper;
                                if (!oldAuthMapperIdMap.containsKey(authority.getServerId() + "_" + authority.getId().toString() + "_" + mapperId)) {
                                    authMapper = new AuthMapperRelation(serverName, authority.getUuid(), mapper.getUuid());
                                    session.save(authMapper);
                                }
                            } else {
                                throw new RuntimeException("ServerName = " + serverName + " MapperId = " + mapperId + "   未找到对应权限");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    /**
     * 删除数据库多余的 Mapper
     *
     * @param session      session
     * @param oldMapperMap 数据库中查询所得的Mapper
     */
    private void deleteUselessMapper(Session session, Map<String, Mapper> oldMapperMap) {
        Map<String, Mapper> modernMapperMap = controllerMap.entrySet().stream()
                .map(entry -> {
                            String bundleId = entry.getValue().getClass().getAnnotation(Controller.class).bundleId()[0];
                            Method[] methods = entry.getValue().getClass().getMethods();
                            return Arrays.stream(methods)
                                    .filter(this.isMapperMethod)
                                    .map(method -> this.getMethodMapper(bundleId, method))
                                    .collect(Collectors.toList());
                        }

                )
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(item -> item.getBundleId() + item.getActionId(), item -> item));
        oldMapperMap.entrySet().stream()
                .filter((item -> !modernMapperMap.containsKey(item.getKey())))
                .forEach(item -> {
                    try {
                        Mapper mapper = item.getValue();
                        Criteria criteria = session.creatCriteria(AuthMapperRelation.class);
                        criteria.add(Restrictions.equ("mapperUuid", mapper.getUuid()));
                        criteria.delete();
                        session.delete(mapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private Predicate<Method> isMapperMethod = method -> method.getAnnotation(Action.class) != null;

    /**
     * 删除数据库多余的 AuthMapperRelation
     *
     * @param session                  session
     * @param oldAuthMapperRelationMap 数据库查询出的 AuthMapperRelation
     */
    private void deleteUselessAuthMapper(Session session, Map<String, AuthMapperRelation> oldAuthMapperRelationMap) {
        String modernAuthMapperRelationMapKeys = controllerMap.entrySet().stream()
                .map(entry -> {
                            String bundleId = entry.getValue().getClass().getAnnotation(Controller.class).bundleId()[0];
                            Method[] methods = entry.getValue().getClass().getMethods();
                            return Arrays.stream(methods)
                                    .filter(this.isMapperMethod)
                                    .map(method -> {
                                        Mapper mapper = this.getMethodMapper(bundleId, method);
                                        Auth auth = method.getAnnotation(Auth.class);
                                        return this.serverName + "_" + auth.value() + "_" + mapper.getBundleId() + mapper.getActionId();
                                    })
                                    .collect(Collectors.toList());
                        }
                )
                .flatMap(Collection::stream)
                .collect(Collectors.joining(" ,"));
        oldAuthMapperRelationMap.keySet().stream()
                .filter(item -> !modernAuthMapperRelationMapKeys.contains(item))
                .forEach(item -> {
                    try {
                        session.delete(oldAuthMapperRelationMap.get(item));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 获取到类中路由方法
     *
     * @param method method
     * @return Mapper
     */
    private Mapper getMethodMapper(String bundleId, Method method) {
        Action action = method.getAnnotation(Action.class);
        String mapperId = method.getAnnotation(Action.class).actionId()[0];
        String requestMethods = Arrays.stream(action.method()).map(Enum::name).collect(Collectors.joining(","));

        return new Mapper(this.serverName, requestMethods, bundleId, mapperId);
    }
}
