package org.hv.biscuit.controller;

import java.io.Serializable;

/**
 * @author wujianchuan 2019/2/20
 */
public class Body<T> implements Serializable {
    private static final long serialVersionUID = -7606557226712921098L;
    private Boolean success;
    private String code;
    private String category;
    private String title;
    private String message;
    private String msg;
    private T object;

    private Body() {
    }

    private Body(String title, String message, T object) {
        this.title = title;
        this.message = message;
        this.object = object;
    }

    private Body(T object) {
        this.object = object;
    }

    public static <E> Body<E> newDefaultInstance(String title, String message, E object) {
        Body<E> instance = new Body<>(title, message, object);
        instance.setSuccess(true);
        instance.setCode("200");
        instance.setCategory(CategoryTypes.DEFAULT);
        return instance;
    }

    public static <E> Body<E> newPrimaryInstance(String title, String message, E object) {
        Body<E> instance = new Body<>(title, message, object);
        instance.setSuccess(true);
        instance.setCode("200");
        instance.setCategory(CategoryTypes.PRIMARY);
        return instance;
    }

    public static <E> Body<E> newSuccessInstance(String title, String message, E object) {
        Body<E> instance = new Body<>(title, message, object);
        instance.setSuccess(true);
        instance.setCode("200");
        instance.setCategory(CategoryTypes.SUCCESS);
        return instance;
    }

    public static <E> Body<E> newSuccessInstance(E object) {
        Body<E> instance = new Body<>(object);
        instance.setSuccess(true);
        return instance;
    }

    public static <E> Body<E> newInfoInstance(String title, String message, E object) {
        Body<E> instance = new Body<>(title, message, object);
        instance.setSuccess(true);
        instance.setCode("200");
        instance.setCategory(CategoryTypes.INFO);
        return instance;
    }

    public static <E> Body<E> newWaringInstance(String title, String message, E object) {
        Body<E> instance = new Body<>(title, message, object);
        instance.setSuccess(false);
        instance.setCode("500");
        instance.setMsg(message);
        instance.setCategory(CategoryTypes.WARING);
        return instance;
    }

    public static <E> Body<E> newErrorInstance(String title, String message, E object) {
        Body<E> instance = new Body<>(title, message, object);
        instance.setSuccess(false);
        instance.setCode("500");
        instance.setMsg(message);
        instance.setCategory(CategoryTypes.ERROR);
        return instance;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
