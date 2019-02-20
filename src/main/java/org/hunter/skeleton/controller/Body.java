package org.hunter.skeleton.controller;

/**
 * @author wujianchuan 2019/2/20
 */
public class Body {
    private Boolean success;
    private String category;
    private String title;
    private String message;
    private Object object;

    private Body(String title, String message, Object object) {
        this.title = title;
        this.message = message;
        this.object = object;
    }

    public static Body newDefaultInstance(String title, String message, Object object) {
        Body instance = new Body(title, message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.DEFAULT);
        return instance;
    }

    public static Body newPrimaryInstance(String title, String message, Object object) {
        Body instance = new Body(title, message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.PRIMARY);
        return instance;
    }

    public static Body newSuccessInstance(String title, String message, Object object) {
        Body instance = new Body(title, message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.SUCCESS);
        return instance;
    }

    public static Body newInfoInstance(String title, String message, Object object) {
        Body instance = new Body(title, message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.INFO);
        return instance;
    }

    public static Body newWaringInstance(String title, String message, Object object) {
        Body instance = new Body(title, message, object);
        instance.setSuccess(false);
        instance.setCategory(CategoryTypes.WARING);
        return instance;
    }

    public static Body newErrorInstance(String title, String message, Object object) {
        Body instance = new Body(title, message, object);
        instance.setSuccess(false);
        instance.setCategory(CategoryTypes.ERROR);
        return instance;
    }

    public Boolean getSuccess() {
        return success;
    }

    private void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCategory() {
        return category;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
