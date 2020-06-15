package org.hv.biscuits.core;

import org.hv.biscuits.core.views.ActionView;
import org.hv.biscuits.core.views.BundleView;
import org.hv.biscuits.core.views.PermissionView;
import org.hv.biscuits.domain.process.ProcessNodeConfig;
import org.hv.biscuits.service.ServiceConfig;
import org.hv.biscuits.spine.model.Authority;
import org.hv.biscuits.spine.model.Bundle;
import org.hv.biscuits.spine.model.Mapper;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.lunch.PocketConfig;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.hv.pocket.session.Transaction;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author leyan95
 */
@Component
public class BiscuitsConfigDefault implements BiscuitsConfig {

    private final PocketConfig pocketConfig;
    private final ActionFactory actionFactory;
    private final ProcessNodeConfig processNodeConfig;
    private final ServiceConfig serviceConfig;
    private boolean withPersistence;

    private Session session;
    private boolean runWithDevelopEnvironment;

    public BiscuitsConfigDefault(PocketConfig pocketConfig, ActionFactory actionFactory, ProcessNodeConfig processNodeConfig, ServiceConfig serviceConfig) {
        this.pocketConfig = pocketConfig;
        this.actionFactory = actionFactory;
        this.processNodeConfig = processNodeConfig;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public BiscuitsConfig init(boolean withPersistence) throws Exception {
        this.withPersistence = withPersistence;
        if (this.withPersistence) {
            this.pocketConfig.init();
            this.processNodeConfig.init();
        }
        this.serviceConfig.init();
        this.actionFactory.init();
        return this;
    }

    @Override
    public Map<String, BundleView> getActionMap() {
        return this.actionFactory.getActionMap();
    }

    @Override
    public Map<String, PermissionView> getPermissionMap() {
        return this.actionFactory.getPermissionMap();
    }

    @Override
    public BiscuitsConfig persistenceMapper(String serviceId, Map<String, BundleView> bundleViewMap) throws SQLException {
        if (!this.withPersistence) {
            throw new IllegalAccessError("程序未开启持久化功能");
        }
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

    @Override
    public BiscuitsConfig resetPersistencePermission(String serviceId, Map<String, PermissionView> permissionMap) throws SQLException {
        if (!this.withPersistence) {
            throw new IllegalAccessError("程序未开启持久化功能");
        }
        this.session.open();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (runWithDevelopEnvironment) {
            this.dynamicPersistencePermission(serviceId, permissionMap);
        } else {
            this.producePersistencePermission(serviceId, permissionMap);
        }

        transaction.commit();
        this.session.close();
        return this;
    }

    @Override
    public BiscuitsConfig runWithDevelopEnvironment() {
        this.runWithDevelopEnvironment = true;
        return this;
    }

    @Override
    public BiscuitsConfig setBiscuitsDatabaseSessionId(String sessionId) {
        this.session = SessionFactory.getSession(sessionId);
        return this;
    }

    private void dynamicPersistenceMapper(String serviceId, Map<String, BundleView> bundleViewMap) throws SQLException {
        // 查询所有的bundle
        Criteria criteria = this.session.createCriteria(Bundle.class);
        criteria.add(Restrictions.equ("serverId", serviceId));
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
                        .add(Restrictions.equ("serverId", serviceId));
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
        criteria.add(Restrictions.equ("serverId", serviceId));
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
