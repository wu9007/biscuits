package org.hunter.skeleton.controller;

/**
 * @author wujianchuan 2019/2/20
 */
public class ResponseBody {
    private Boolean success;
    private String category;
    private String title;
    private String message;
    private Object object;

    private ResponseBody(String title, String message, Object object) {
        this.title = title;
        this.message = message;
        this.object = object;
    }

    public static ResponseBody newSuccessInstance(String title, String message, Object object) {
        ResponseBody instance = new ResponseBody(title, message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.INFO);
        return instance;
    }

    public static ResponseBody newWaringInstance(String title, String message, Object object) {
        ResponseBody instance = new ResponseBody(title, message, object);
        instance.setSuccess(false);
        instance.setCategory(CategoryTypes.WARING);
        return instance;
    }

    public static ResponseBody newErrorInstance(String title, String message, Object object) {
        ResponseBody instance = new ResponseBody(title, message, object);
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
