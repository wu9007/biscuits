package org.hv.biscuits.launcher;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.spine.model.AuthMapperRelation;
import org.hv.biscuits.spine.model.Authority;
import org.hv.biscuits.spine.model.Bundle;
import org.hv.biscuits.spine.model.Mapper;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.hv.pocket.session.Transaction;
import org.hv.biscuits.annotation.Auth;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.bundle.AbstractBundle;
import org.hv.biscuits.bundle.BundleFactory;
import org.hv.biscuits.controller.AbstractController;
import org.hv.biscuits.permission.AbstractPermission;
import org.hv.biscuits.permission.PermissionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan
 */
@SuppressWarnings("unchecked")
@Component
@Order(1)
public class Launcher implements CommandLineRunner {

    private static final String UNDERLINE_DIVIDER = "_";

    private final String serverId;
    private final List<AbstractBundle> bundleContainers;
    private final List<AbstractPermission> permissionContainers;
    private final List<AbstractController> controllerList;

    private Map<String, Bundle> bundleMap = null;
    private Map<String, Authority> permissionMap = null;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public Launcher(@Nullable List<AbstractBundle> bundleContainers, ApplicationContext context, @Nullable List<AbstractController> controllerList,
                    @Nullable List<AbstractPermission> permissionContainers) {
        this.serverId = Objects.requireNonNull(context.getEnvironment().getProperty("spring.application.name")).toUpperCase();
        this.bundleContainers = bundleContainers;
        this.controllerList = controllerList;
        this.permissionContainers = permissionContainers;
    }

    @Override
    public void run(String... args) {
        Session session = SessionFactory.getSession("biscuits");
        session.open();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (this.bundleContainers != null && this.bundleContainers.size() > 0) {
            this.operateBundle(session, transaction);
        }
        if (this.permissionContainers != null && this.permissionContainers.size() > 0) {
            this.operateAuthority(session, transaction);
        }
        if (this.controllerList != null) {
            this.operateMapper(session);
        }
        transaction.commit();
        session.close();
    }

    private void operateBundle(Session session, Transaction transaction) {
        this.bundleContainers.forEach(bundleContainer -> bundleContainer.init());
        Criteria criteria = session.createCriteria(Bundle.class)
                .add(Restrictions.equ("serverId", serverId));
        List<Bundle> bundleList = criteria.list();
        this.bundleMap = bundleList.stream().collect(Collectors.toMap(Bundle::getBundleId, i -> i));

        this.controllerList.forEach(controller -> {
            Controller controllerAnnotation = controller.getClass().getAnnotation(Controller.class);
            String bundleId = controllerAnnotation.bundleId()[0];
            if (!this.bundleMap.containsKey(bundleId)) {
                String bundleName = BundleFactory.getBundleName(bundleId);
                if (bundleName != null) {
                    Bundle bundle = new Bundle(bundleId, BundleFactory.getBundleName(bundleId), serverId, controllerAnnotation.auth());
                    try {
                        session.save(bundle);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    this.bundleMap.put(bundleId, bundle);
                } else {
                    transaction.rollBack();
                    throw new IllegalArgumentException(String.format("缺少 Bundle: %s", bundleId));
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void operateAuthority(Session session, Transaction transaction) {
        this.permissionContainers.forEach(permissionContainer -> permissionContainer.setServerId(this.serverId).init());
        ActionFactory.init(controllerList);
        Criteria criteria = session.createCriteria(Authority.class)
                .add(Restrictions.equ("serverId", this.serverId));
        List<Authority> permissions = criteria.list();
        permissionMap = permissions.stream().collect(Collectors.toMap(Authority::getId, i -> i));

        ActionFactory.getActionMap().forEach((bundleId, actionMap) -> actionMap.forEach((actionId, action) -> {
            Auth auth = action.getAnnotation(Auth.class);
            if (auth != null) {
                String permissionId = auth.value();
                if (!permissionMap.containsKey(permissionId)) {
                    Authority authority = PermissionFactory.get(permissionId);
                    if (authority != null) {
                        try {
                            session.save(authority);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        transaction.rollBack();
                        throw new IllegalArgumentException(String.format("缺少 Permission: %s", permissionId));
                    }
                    permissionMap.put(auth.value(), authority);
                }
            }
        }));

    }

    private void operateMapper(Session session) {
        Criteria criteria = session.createCriteria(Mapper.class)
                .add(Restrictions.equ("serverId", this.serverId));
        List<Mapper> mappers = criteria.list();
        Map<String, Mapper> mapperMap = mappers.stream()
                .collect(Collectors.toMap(mapper -> mapper.getBundleId() + UNDERLINE_DIVIDER + mapper.getActionId() + UNDERLINE_DIVIDER + mapper.getRequestMethod(), i -> i));

        Criteria relationCriteria = session.createCriteria(AuthMapperRelation.class)
                .add(Restrictions.equ("serverId", this.serverId));
        List<AuthMapperRelation> authMapperRelations = relationCriteria.list();
        Map<String, AuthMapperRelation> authMapperRelationMap = authMapperRelations.stream()
                .collect(Collectors.toMap(authMapperRelation -> authMapperRelation.getAuthUuid() + UNDERLINE_DIVIDER + authMapperRelation.getMapperUuid(), i -> i));

        ActionFactory.getActionMap().forEach((bundleId, actionMap) -> actionMap.forEach((actionId, action) -> {
            Action actionAnnotation = action.getAnnotation(Action.class);
            String requestMethod = actionAnnotation.method()[0].toString();
            String mapperIdentification = bundleId + UNDERLINE_DIVIDER + actionId + UNDERLINE_DIVIDER + requestMethod;
            Mapper mapper;
            if (!mapperMap.containsKey(mapperIdentification)) {
                mapper = new Mapper(this.serverId, requestMethod, bundleId, actionId);
                mapper.setBundleUuid(this.bundleMap.get(bundleId).getUuid());
                try {
                    session.save(mapper);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                mapper = mapperMap.get(mapperIdentification);
            }

            Auth authAnnotation = action.getAnnotation(Auth.class);
            if (authAnnotation != null) {
                String authUuid = this.permissionMap.get(authAnnotation.value()).getUuid();
                String mapperUuid = mapper.getUuid();
                if (!authMapperRelationMap.containsKey(authUuid + UNDERLINE_DIVIDER + mapperUuid)) {
                    AuthMapperRelation authMapperRelation = new AuthMapperRelation(this.serverId, authUuid, mapperUuid);
                    try {
                        session.save(authMapperRelation);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }
}
