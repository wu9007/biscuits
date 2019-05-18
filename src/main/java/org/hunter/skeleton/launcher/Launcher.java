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
import org.hunter.skeleton.permission.PermissionFactory;
import org.hunter.skeleton.spine.model.AuthMapperRelation;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Bundle;
import org.hunter.skeleton.spine.model.Mapper;
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
    private final AbstractBundle bundleContainer;
    private final AbstractPermission permissionContainer;
    private final List<AbstractController> controllerList;

    private Map<String, Bundle> bundleMap = null;
    private Map<String, Authority> permissionMap = null;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public Launcher(@Nullable AbstractBundle bundleContainer, ApplicationContext context, @Nullable List<AbstractController> controllerList, @Nullable AbstractPermission permissionContainer) {
        this.serverId = Objects.requireNonNull(context.getEnvironment().getProperty("spring.application.name")).toUpperCase();
        this.bundleContainer = bundleContainer;
        this.controllerList = controllerList;
        this.permissionContainer = permissionContainer;
    }

    @Override
    public void run(String... args) {
        Session session = SessionFactory.getSession("skeleton");
        session.open();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if (this.bundleContainer != null) {
            this.operateBundle(session, transaction);
        }
        if (this.permissionContainer != null) {
            this.operateAuthority(session, transaction);
        }
        if (this.controllerList != null) {
            this.operateMapper(session);
        }
        transaction.commit();
        session.close();
    }

    private void operateBundle(Session session, Transaction transaction) {
        this.bundleContainer.init();
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
                    throw new NullPointerException(String.format("缺少 Bundle: %s", bundleId));
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void operateAuthority(Session session, Transaction transaction) {
        this.permissionContainer.setServerId(this.serverId).init();
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
                        throw new NullPointerException(String.format("缺少 Permission: %s", permissionId));
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
