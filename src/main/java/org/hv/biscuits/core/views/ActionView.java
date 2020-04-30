package org.hv.biscuits.core.views;

/**
 * @author wujianchuan
 */
public class ActionView {
    private final String actionId;
    private final String requestMethod;
    private final String authId;

    private ActionView(String actionId, String requestMethod, String authId) {
        this.actionId = actionId;
        this.requestMethod = requestMethod;
        this.authId = authId;
    }

    public static ActionView build(String actionId, String requestMethod, String authId) {
        return new ActionView(actionId, requestMethod, authId);
    }

    public String getActionId() {
        return actionId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getAuthId() {
        return authId;
    }
}
