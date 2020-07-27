package org.hv.biscuits.controller;

import java.io.Serializable;

/**
 * @author leyan95 2019/2/20
 */
public class Body implements Serializable {
    private static final long serialVersionUID = -7606557226712921098L;
    private boolean success;
    private String category;
    private Object data;
    private String title;
    private String message;
    private boolean reLogin;

    public Body() {
    }

    public static Body success() {
        return new Body(true, CategoryTypes.SUCCESS);
    }

    public static Body info() {
        return new Body(true, CategoryTypes.INFO);
    }

    public static Body warning() {
        return new Body(true, CategoryTypes.WARING);
    }

    public static Body error() {
        return new Body(false, CategoryTypes.ERROR);
    }

    public Body title(String title) {
        this.title = title;
        return this;
    }

    public Body message(String message) {
        this.message = message;
        return this;
    }

    public Body data(Object data) {
        this.data = data;
        return this;
    }

    public Body reLogin() {
        this.reLogin = true;
        return this;
    }

    private Body(boolean success, String category) {
        this.success = success;
        this.category = category;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCategory() {
        return category;
    }

    public Object getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean getReLogin() {
        return reLogin;
    }
}
