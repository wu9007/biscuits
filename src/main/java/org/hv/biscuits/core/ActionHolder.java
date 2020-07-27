package org.hv.biscuits.core;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.core.views.ActionView;
import org.hv.biscuits.core.views.BundleView;
import org.hv.biscuits.core.views.PermissionView;
import org.hv.biscuits.permission.Permission;
import org.hv.biscuits.spine.model.Authority;
import org.hv.biscuits.spine.model.Bundle;
import org.hv.biscuits.spine.model.Mapper;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.hv.pocket.session.Transaction;
import org.springframework.aop.framework.AopProxyUtils;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author leyan95
 */
public class ActionHolder {
    private final static ActionHolder INSTANCE = new ActionHolder();
    private final static Map<String, BundleView> ACTION_MAP = new ConcurrentHashMap<>();
    private final static Map<String, PermissionView> PERMISSION_MAP = new ConcurrentHashMap<>();
    private boolean runWithDevelopEnvironment;
    private String ownServiceId;
    private Session session;

    private ActionHolder() {
    }

    public static ActionHolder getInstance() {
        return INSTANCE;
    }

    /**
     * 设置运行环境
     * 开发环境下路由和权限的关联在每次重启时根据编码进行对应
     * 生产环境下如果不存就直接保存 如果存在就不进行任何修改（因为可能已经通过客户端进行了增加和编辑————记录了sql的脚本）
     *
     * @return Action Factory
     */
    public ActionHolder runWithDevelopEnvironment() {
        this.runWithDevelopEnvironment = true;
        return this;
    }

    /**
     * 设置在持久化路由和权限数据时使用的数据库会话
     *
     * @param sessionId 数据库会话ID
     * @return Action Factory
     */
    public ActionHolder setBiscuitsDatabaseSessionId(String sessionId) {
        this.session = SessionFactory.getSession(sessionId);
        return this;
    }

    /**
     * 格式话收集过来的 Bundle 和 Permission
     *
     * @param ownServiceId 本服务的ID
     * @param bundleList   Bundle 集合
     * @param permissions  Permission 集合
     */
    protected void init(String ownServiceId, List<Object> bundleList, List<Permission> permissions) {
        this.ownServiceId = ownServiceId.toUpperCase();
        permissions.forEach(Permission::init);
        bundleList.forEach(controller -> {
            Class<?> clazz = AopProxyUtils.ultimateTargetClass(controller);
            Controller controllerAnnotation = clazz.getAnnotation(Controller.class);
            String bundleId = controllerAnnotation.bundleId()[0];
            String bundleName = controllerAnnotation.name();
            boolean withAuth = controllerAnnotation.auth();
            ACTION_MAP.putIfAbsent(bundleId, BundleView.newInstance(bundleId, bundleName, withAuth));
            BundleView bundleView = ACTION_MAP.get(bundleId);
            for (Method method : clazz.getDeclaredMethods()) {
                Action actionAnnotation = method.getAnnotation(Action.class);
                if (actionAnnotation != null) {
                    String authId = actionAnnotation.authId();
                    if (authId.isEmpty()) {
                        authId = null;
                    }
                    if (authId != null) {
                        if (!PERMISSION_MAP.containsKey(authId)) {
                            throw new IllegalArgumentException(String.format("未找到权限信息 %s", authId));
                        } else {
                            PermissionView permissionView = PERMISSION_MAP.get(authId);
                            if (permissionView.getBundleId() == null) {
                                permissionView.setBundleId(bundleId);
                            }
                        }
                    }
                    bundleView.appendActionView(ActionView.build(actionAnnotation.actionId()[0], actionAnnotation.method()[0].toString(), authId));
                }
            }
        });
    }

    /**
     * 获取本服务ID
     *
     * @return 本服务ID
     */
    public String getOwnServiceId() {
        return ownServiceId;
    }

    /**
     * 获取格式化完成的路由数据
     *
     * @return Map
     */
    public Map<String, BundleView> getActionMap() {
        return new ConcurrentHashMap<>(ACTION_MAP);
    }

    /**
     * 获取格式化完成的权限数据
     *
     * @return Map
     */
    public Map<String, PermissionView> getPermissionMap() {
        return new ConcurrentHashMap<>(PERMISSION_MAP);
    }

    /**
     * 注册的权限
     *
     * @param id     权限编号
     * @param name   权限名称
     * @param common 描述
     */
    public static void register(String id, String name, String common) {
        PermissionView permissionView = PERMISSION_MAP.putIfAbsent(id, PermissionView.newInstance(id, name, common));
        if (permissionView != null) {
            throw new IllegalArgumentException(String.format("权限重复 %s", permissionView.getId()));
        }
    }

    /**
     * 持久化给定服务的路由数据
     *
     * @param serviceId     服务ID
     * @param bundleViewMap 路由数据
     * @return Action Factory
     * @throws SQLException e
     */
    public ActionHolder persistenceMapper(String serviceId, Map<String, BundleView> bundleViewMap) throws SQLException {
        this.session.open();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (runWithDevelopEnvironment) {
            this.dynamicPersistenceMapper(serviceId, bundleViewMap);
        } else {
            this.producePersistenceMapper(serviceId, bundleViewMap);
        }

        transaction.commit();
        this.session.close();
        return this;
    }

    /**
     * 持久化给定服务的权限信息
     *
     * @param serviceId         服务ID
     * @param permissionViewMap 权限数据
     * @return Action Factory
     * @throws SQLException e
     */
    public ActionHolder persistencePermission(String serviceId, Map<String, PermissionView> permissionViewMap) throws SQLException {
        this.session.open();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (runWithDevelopEnvironment) {
            this.dynamicPersistencePermission(serviceId, permissionViewMap);
        } else {
            this.producePersistencePermission(serviceId, permissionViewMap);
        }

        transaction.commit();
        this.session.close();
        return this;
    }

    private void dynamicPersistenceMapper(String serviceId, Map<String, BundleView> bundleViewMap) throws SQLException {
        // 查询所有的bundle
        Criteria criteria = this.session.createCriteria(Bundle.class);
        criteria.add(Restrictions.equ("serviceId", serviceId));
        List<Bundle> bundles = criteria.list();
        Map<String, Bundle> bundleMap = bundles.stream().collect(Collectors.toMap(Bundle::getBundleId, item -> item));
        for (Map.Entry<String, BundleView> stringBundleViewEntry : bundleViewMap.entrySet()) {
            BundleView bundleView = stringBundleViewEntry.getValue();
            Bundle bundle;
            // 数据库中已存在该bundle（更新）
            if (bundleMap.containsKey(bundleView.getBundleId())) {
                bundle = bundleMap.get(bundleView.getBundleId());
                if (!bundle.getBundleName().equals(bundleView.getBundleName()) || !bundle.getWithAuth().equals(bundleView.isWithAuth())) {
                    bundle.setBundleName(bundleView.getBundleName());
                    bundle.setWithAuth(bundleView.isWithAuth());
                    this.session.update(bundle);
                }
                // 查询所有的action
                criteria = this.session.createCriteria(Mapper.class);
                criteria.add(Restrictions.equ("bundleId", bundleView.getBundleId()))
                        .add(Restrictions.equ("serviceId", serviceId));
                List<Mapper> mappers = criteria.list();
                Map<String, Mapper> actionMap = mappers.stream().collect(Collectors.toMap(Mapper::getActionId, item -> item));
                for (Map.Entry<String, ActionView> stringActionViewEntry : bundleView.getActionViews().entrySet()) {
                    ActionView actionView = stringActionViewEntry.getValue();
                    Mapper action;
                    // 数据库中已存在该action（更新）
                    if (actionMap.containsKey(actionView.getActionId())) {
                        action = actionMap.get(actionView.getActionId());
                        if (!action.getRequestMethod().equals(actionView.getRequestMethod()) || !Objects.equals(actionView.getAuthId(), action.getAuthId())) {
                            action.setAuthId(actionView.getAuthId());
                            action.setRequestMethod(actionView.getRequestMethod());
                            this.session.update(action);
                        }
                        // 数据库中不存在该action（新增）
                    } else {
                        action = new Mapper(serviceId, actionView.getRequestMethod(), bundle.getBundleId(), actionView.getActionId(), actionView.getAuthId());
                        action.setBundleUuid(bundle.getUuid());
                        this.session.save(action);
                    }
                }
                // 数据库中不存在bundle（新增）
            } else {
                bundle = new Bundle(bundleView.getBundleId(), bundleView.getBundleName(), serviceId, bundleView.isWithAuth());
                this.session.save(bundle);
                for (ActionView actionView : bundleView.getActionViews().values()) {
                    Mapper action = new Mapper(serviceId, actionView.getRequestMethod(), bundle.getBundleId(), actionView.getActionId(), actionView.getAuthId());
                    action.setBundleUuid(bundle.getUuid());
                    this.session.save(action);
                }
            }
        }
    }

    private void dynamicPersistencePermission(String serviceId, Map<String, PermissionView> permissionViewMap) throws SQLException {
        Criteria criteria = this.session.createCriteria(Authority.class);
        criteria.add(Restrictions.equ("serviceId", serviceId));
        List<Authority> authorities = criteria.list();
        Map<String, Authority> permissionMap = authorities.stream().collect(Collectors.toMap(Authority::getId, item -> item));
        for (Map.Entry<String, PermissionView> stringPermissionViewEntry : permissionViewMap.entrySet()) {
            PermissionView permissionView = stringPermissionViewEntry.getValue();
            Authority permission;
            if (permissionMap.containsKey(permissionView.getId())) {
                permission = permissionMap.get(permissionView.getId());
                if (!Objects.equals(permission.getName(), permissionView.getName()) || !Objects.equals(permission.getComment(), permissionView.getComment()) || !Objects.equals(permissionView.getBundleId(), permission.getBundleId())) {
                    permission.setBundleId(permissionView.getBundleId());
                    permission.setName(permissionView.getName());
                    permission.setComment(permissionView.getComment());
                    this.session.update(permission);
                }
            } else {
                permission = new Authority(serviceId, permissionView.getBundleId(), permissionView.getId(), permissionView.getName(), permissionView.getComment());
                this.session.save(permission);
            }
        }

    }

    private void producePersistenceMapper(String serviceId, Map<String, BundleView> bundleViewMap) {
        // TODO 生产环境下如果不存就直接保存 如果存在就不进行任何修改（因为可能已经通过客户端进行了增加和编辑————记录了sql的脚本）
    }

    private void producePersistencePermission(String serviceId, Map<String, PermissionView> permissionViewMap) {
        // TODO 生产环境下如果不存就直接保存 如果存在就不进行任何修改（因为可能已经通过客户端进行了增加和编辑————记录了sql的脚本）
    }
}
