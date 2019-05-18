package org.hunter.skeleton.launcher;

import org.hunter.skeleton.annotation.Action;
import org.hunter.skeleton.annotation.Controller;
import org.hunter.skeleton.controller.AbstractController;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujianchuan
 */
class ActionFactory {
    private final static Map<String, Map<String, Method>> ACTION_MAP = new LinkedHashMap<>();

    protected static void init(List<AbstractController> controllers) {
        controllers.forEach(controller -> {
            Class clazz = controller.getClass();
            Controller controllerAnnotation = (Controller) clazz.getAnnotation(Controller.class);
            String bundleId = controllerAnnotation.bundleId()[0];
            Map<String, Method> methodMap;
            if (ACTION_MAP.containsKey(bundleId)) {
                methodMap = ACTION_MAP.get(bundleId);
            } else {
                methodMap = new LinkedHashMap<>();
                ACTION_MAP.put(bundleId, methodMap);
            }
            for (Method method : clazz.getDeclaredMethods()) {
                Action actionAnnotation = method.getAnnotation(Action.class);
                if (actionAnnotation != null) {
                    methodMap.put(actionAnnotation.actionId()[0], method);
                }
            }
        });
    }

    protected static Map<String, Map<String, Method>> getActionMap() {
        return new LinkedHashMap<>(ACTION_MAP);
    }
}
