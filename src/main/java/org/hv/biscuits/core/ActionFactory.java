package org.hv.biscuits.core;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.core.views.ActionView;
import org.hv.biscuits.core.views.BundleView;
import org.hv.biscuits.core.views.PermissionView;
import org.hv.biscuits.permission.Permission;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan
 */
@Component
public class ActionFactory {
    private final static Map<String, BundleView> ACTION_MAP = new ConcurrentHashMap<>();
    private final static Map<String, PermissionView> PERMISSION_MAP = new ConcurrentHashMap<>();

    private final List<Object> bundleList;
    private final List<Permission> permissions;

    ActionFactory(ApplicationContext applicationContext, List<Permission> permissions) {
        bundleList = Arrays.asList(applicationContext.getBeansWithAnnotation(Controller.class).values().toArray());
        this.permissions = permissions;
    }

    protected void init() {
        this.permissions.forEach(Permission::init);
        bundleList.forEach(controller -> {
            Class<?> clazz = controller.getClass();
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
                    if (authId != null && !PERMISSION_MAP.containsKey(authId)) {
                        throw new IllegalArgumentException(String.format("未找到权限信息 %s", authId));
                    }
                    bundleView.appendActionView(ActionView.build(actionAnnotation.actionId()[0], actionAnnotation.method()[0].toString(), authId));
                }
            }
        });
    }

    public static void register(String id, String name, String common) {
        PermissionView permissionView = PERMISSION_MAP.putIfAbsent(id, PermissionView.newInstance(id, name, common));
        if (permissionView != null) {
            throw new IllegalArgumentException(String.format("权限重复 %s", permissionView.getId()));
        }
    }

    protected Map<String, BundleView> getActionMap() {
        return new ConcurrentHashMap<>(ACTION_MAP);
    }

    protected Map<String, PermissionView> getPermissionMap() {
        return new ConcurrentHashMap<>(PERMISSION_MAP);
    }
}
