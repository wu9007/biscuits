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

    public static ResponseBody newDefaultInstance(String title, String message, Object object) {
        ResponseBody instance = new ResponseBody(title, message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.DEFAULT);
        return instance;
    }

    public static ResponseBody newPrimaryInstance(String message, Object object) {
        ResponseBody instance = new ResponseBody("注意", message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.PRIMARY);
        return instance;
    }

    public static ResponseBody newSuccessInstance(String message, Object object) {
        ResponseBody instance = new ResponseBody("成功", message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.SUCCESS);
        return instance;
    }

    public static ResponseBody newInfoInstance(String message, Object object) {
        ResponseBody instance = new ResponseBody("提醒", message, object);
        instance.setSuccess(true);
        instance.setCategory(CategoryTypes.INFO);
        return instance;
    }

    public static ResponseBody newWaringInstance(String message, Object object) {
        ResponseBody instance = new ResponseBody("警告", message, object);
        instance.setSuccess(false);
        instance.setCategory(CategoryTypes.WARING);
        return instance;
    }

    public static ResponseBody newDangerInstance(String message, Object object) {
        ResponseBody instance = new ResponseBody("错误", message, object);
        instance.setSuccess(false);
        instance.setCategory(CategoryTypes.DANGER);
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
