package org.hv.biscuits.core.views;

import java.io.Serializable;

/**
 * @author leyan95
 */
public class ActionView implements Serializable {
    private static final long serialVersionUID = -123733280550004811L;
    private String actionId;
    private String requestMethod;
    private String authId;

    public ActionView() {
    }

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

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }
}
