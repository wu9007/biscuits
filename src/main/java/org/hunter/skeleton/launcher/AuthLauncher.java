package org.hunter.skeleton.launcher;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionFactory;
import org.hunter.pocket.session.Transaction;
import org.hunter.skeleton.annotation.Action;
import org.hunter.skeleton.annotation.Auth;
import org.hunter.skeleton.annotation.Controller;
import org.hunter.skeleton.bundle.AbstractBundle;
import org.hunter.skeleton.bundle.BundleFactory;
import org.hunter.skeleton.controller.AbstractController;
import org.hunter.skeleton.permission.AbstractPermission;
import org.hunter.skeleton.spine.model.AuthMapperRelation;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Bundle;
import org.hunter.skeleton.spine.model.BundleGroupRelation;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.permission.PermissionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
    List<AbstractPermission> permissionList;

    private final
    List<AbstractBundle> bundleList;

    private final String serverId;
    private Map<String, Mapper> oldMapperMap;
    private Map<String, AuthMapperRelation> oldAuthMapperRelationMap;
    private Map<String, Bundle> bundleMap;

    @Autowired
    public AuthLauncher(Map<String, AbstractController> controllerMap, @Nullable List<AbstractPermission> permissionList, @Nullable List<AbstractBundle> bundleList, ApplicationContext context) {
        this.controllerMap = controllerMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.permissionList = permissionList;
        this.bundleList = bundleList;
        this.serverId = Objects.requireNonNull(context.getEnvironment().getProperty("spring.application.name")).toUpperCase();
    }

    @Override
    public void run(String... args) throws Exception {
        Session session = SessionFactory.getSession("skeleton");
        session.open();
        Transaction transactional = session.getTransaction();
        transactional.begin();
        this.execute(session);
        transactional.commit();
        session.close();
    }

    private void execute(Session session) throws Exception {
        //把数据库中的权限放入到工厂缓存起来
        PermissionFactory.init(this.getPermissionMap(session));

        //保存新增权限并放入到缓存中
        if (this.permissionList != null) {
            this.permissionList.forEach(permission -> {
                permission.setServerId(this.serverId);
                permission.setSession(session);
                permission.init();
            });
        }

        //删除多余的 permission 及相关数据
        PermissionFactory.removeUselessPermission(session);

        //将Bundle放入到缓存中
        if (this.bundleList != null) {
            this.bundleList.forEach(bundle -> {
                bundle.setServerId(this.serverId);
                bundle.init();
            });
        }

        //获取数据库中的bundle对象，以键值对的方式返回，key-> serverId + "_" + bundleId
        this.bundleMap = this.getBundleMap(session);

        //获取数据库中的Mapper对象，以键值对的方式返回，key-> bundleId + actionId
        this.oldMapperMap = this.getMapperMap(session);

        if (this.oldMapperMap != null && this.oldMapperMap.size() > 0) {
            //删除无用的 mapper 及相关数据
            this.deleteUselessMapper(session);
        }

        //获取数据库中权限和路由关联表对象，以键值对的方式返回，key->serverId + authorityId + bundleId + actionId
        this.oldAuthMapperRelationMap = this.getAuthMapperRelationMap(session);

        if (this.oldAuthMapperRelationMap != null && this.oldAuthMapperRelationMap.size() > 0) {
            //删除多余的权限和路由的关联对象
            this.deleteUselessAuthMapper(session);
        }

        //保存bundle、auth、mapper
        this.saveBundleAuthAndMapper(session);

        //删除多余的bundle
        this.deleteUselessBundle(session);

        //将Bundle注册到工厂中
        this.registerBundleToFactory();
    }

    /**
     * 拿到数据库中的已有权限
     *
     * @param session session
     * @return 权限集合
     * @throws Exception e
     */
    private Map<String, Authority> getPermissionMap(Session session) throws Exception {
        Criteria criteria = session.createCriteria(Authority.class);
        criteria.add(Restrictions.equ("serverId", serverId));
        List<Authority> authorityList = criteria.list();
        return authorityList.stream().collect(Collectors.toMap(item -> item.getServerId() + "_" + item.getId(), item -> item));
    }

    /**
     * 获取数据库中的bundle
     *
     * @param session session.
     * @return key -> serverId + bundleId, value -> bundle
     * @throws Exception e
     */
    private Map<String, Bundle> getBundleMap(Session session) throws Exception {
        Criteria criteria = session.createCriteria(Bundle.class);
        criteria.add(Restrictions.equ("serverId", serverId));
        List<Bundle> bundles = criteria.list();
        return bundles.stream()
                .collect(Collectors.toMap(item -> item.getServerId() + "_" + item.getBundleId(), item -> item));
    }

    /**
     * 从数据库中获取所有映射 Mapper
     *
     * @param session session
     * @return 所有映射 Mapper
     * @throws Exception e
     */
    private Map<String, Mapper> getMapperMap(Session session) throws Exception {
        Criteria mapperCriteria = session.createCriteria(Mapper.class);
        mapperCriteria.add(Restrictions.equ("serverId", serverId));
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
    private Map<String, AuthMapperRelation> getAuthMapperRelationMap(Session session) throws Exception {
        Criteria criteria = session.createCriteria(AuthMapperRelation.class);
        criteria.add(Restrictions.equ("serverId", serverId));
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
     * 保存数据库中没有的 Bundle Mapper 和 Authority
     *
     * @param session session
     */
    private void saveBundleAuthAndMapper(Session session) {
        controllerMap.forEach((k, v) -> {
            Controller controllerAnnotation = v.getClass().getAnnotation(Controller.class);
            String bundleId = controllerAnnotation.bundleId()[0];
            boolean withAuth = controllerAnnotation.auth();
            Method[] methods = v.getClass().getMethods();

            String bundleMapKey = serverId + "_" + bundleId;
            if (!this.bundleMap.containsKey(bundleMapKey)) {
                String bundleName = BundleFactory.getBundleName(serverId, bundleId);
                if (bundleName != null && bundleName.length() > 0) {
                    Bundle bundle = new Bundle(bundleId, BundleFactory.getBundleName(serverId, bundleId), serverId, withAuth);
                    session.save(bundle);
                    this.bundleMap.put(bundleMapKey, bundle);
                } else {
                    throw new RuntimeException("请正确注册bundle, id: " + bundleId);
                }
            }

            Arrays.stream(methods)
                    .filter(this.isMapperMethod)
                    .forEach(method -> {
                        Mapper mapper = this.getMethodMapper(bundleId, method);
                        String mapperMapKey = mapper.getBundleId() + mapper.getActionId();
                        try {
                            Auth auth = method.getAnnotation(Auth.class);
                            if (this.oldMapperMap.containsKey(mapperMapKey)) {
                                mapper = this.oldMapperMap.get(mapperMapKey);
                            } else {
                                mapper.setBundleUuid(this.bundleMap.get(bundleMapKey).getUuid());
                                session.save(mapper);
                            }

                            if (withAuth && auth != null) {
                                String auhId = auth.value();
                                Authority authority = PermissionFactory.get(serverId, auhId);
                                if (authority != null) {
                                    AuthMapperRelation authMapperRelation;
                                    if (!this.oldAuthMapperRelationMap.containsKey(authority.getServerId() + "_" + authority.getId() + "_" + mapperMapKey)) {
                                        authMapperRelation = new AuthMapperRelation(serverId, authority.getUuid(), mapper.getUuid());
                                        session.save(authMapperRelation);
                                    }
                                } else {
                                    throw new RuntimeException("serverId = " + serverId + " MapperId = " + mapperMapKey + "   未找到对应权限");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    /**
     * 删除无用的bundle
     *
     * @param session session.
     * @throws Exception e.
     */
    private void deleteUselessBundle(Session session) throws Exception {
        Criteria mapperCriteria = session.createCriteria(Mapper.class);
        mapperCriteria.add(Restrictions.equ("serverId", this.serverId));
        List<Mapper> newMapperList = mapperCriteria.list();
        Map<String, Long> mapperMap = newMapperList.stream()
                .collect(Collectors.groupingBy(mapper -> mapper.getServerId() + "_" + mapper.getBundleId(), Collectors.counting()));
        List<String> uselessKey = new ArrayList<>();
        this.bundleMap.forEach((k, bundle) -> {
            if (mapperMap.get(k) == null) {
                session.createCriteria(BundleGroupRelation.class)
                        .add(Restrictions.equ("bundleUuid", bundle.getUuid()))
                        .delete();
                session.delete(bundle);
                uselessKey.add(k);
            }
        });
        uselessKey.forEach(key -> this.bundleMap.remove(key));
    }

    /**
     * 将Bundle注册到工厂中
     */
    private void registerBundleToFactory() {
        this.bundleMap.values().forEach(BundleFactory::registerBundle);
    }

    /**
     * 删除数据库多余的 Mapper
     *
     * @param session session
     */
    private void deleteUselessMapper(Session session) {
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
                        session.createCriteria(AuthMapperRelation.class)
                                .add(Restrictions.equ("mapperUuid", mapper.getUuid()))
                                .delete();
                        session.delete(mapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private final Predicate<Method> isMapperMethod = method -> method.getAnnotation(Action.class) != null;
    private final Predicate<Method> isAuthMethod = method -> method.getAnnotation(Auth.class) != null;

    /**
     * 删除数据库多余的 AuthMapperRelation
     *
     * @param session session
     */
    private void deleteUselessAuthMapper(Session session) {
        String modernAuthMapperRelationMapKeys = controllerMap.entrySet().stream()
                .map(entry -> {
                            String bundleId = entry.getValue().getClass().getAnnotation(Controller.class).bundleId()[0];
                            Method[] methods = entry.getValue().getClass().getMethods();
                            return Arrays.stream(methods)
                                    .filter(this.isMapperMethod)
                                    .filter(this.isAuthMethod)
                                    .map(method -> {
                                        Mapper mapper = this.getMethodMapper(bundleId, method);
                                        Auth auth = method.getAnnotation(Auth.class);
                                        return this.serverId + "_" + auth.value() + "_" + mapper.getBundleId() + mapper.getActionId();
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

        return new Mapper(this.serverId, requestMethods, bundleId, mapperId);
    }
}
