package org.hv.biscuits.core;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Auth;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.core.views.ActionView;
import org.hv.biscuits.core.views.BundleView;
import org.hv.biscuits.core.views.PermissionView;
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

    ActionFactory(ApplicationContext applicationContext) {
        bundleList = Arrays.asList(applicationContext.getBeanNamesForAnnotation(Controller.class));
    }

    protected void init() {
        bundleList.forEach(controller -> {
            Class<?> clazz = controller.getClass();
            Controller controllerAnnotation = clazz.getAnnotation(Controller.class);
            String bundleId = controllerAnnotation.bundleId()[0];
            String bundleName = controllerAnnotation.name();
            boolean withAuth = controllerAnnotation.auth();
            ACTION_MAP.putIfAbsent(bundleId, BundleView.newInstance(bundleId, bundleName, withAuth));
            BundleView bundleView = ACTION_MAP.get(bundleId);
            for (Method method : clazz.getDeclaredMethods()) {
                Auth authAnnotation = method.getAnnotation(Auth.class);
                Action actionAnnotation = method.getAnnotation(Action.class);
                if (actionAnnotation != null) {
                    ActionView actionView = bundleView.appendActionView(ActionView.build(actionAnnotation.actionId()[0], actionAnnotation.method()[0].toString(), authAnnotation.value()));
                    if (actionView != null) {
                        throw new IllegalArgumentException(String.format("存在拥有相同映射的Action %s/%s", bundleView.getBundleId(), actionView.getActionId()));
                    }
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
