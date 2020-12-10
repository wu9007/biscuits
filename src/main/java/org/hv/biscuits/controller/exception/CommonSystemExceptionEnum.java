package org.hv.biscuits.controller.exception;

/**
 * @author wujianchuan 2020/9/28 10:55
 */
public enum CommonSystemExceptionEnum {
    /**
     * 系统异常
     */
    SYS_ACCESS("F100001", "系统访问异常，请联系相关人员。"),
    PREFERENCE_NOT_FOUND("FS100002", "未找到系统参数为【%s】的设置信息，请确认。"),
    LOGIN_TIME_OUT("FS100003", "您已经长时间没有操作，请点击确定后返回登录页面。"),
    UNKNOWN_TOKEN("FS100004", "用户名或密码错误。");
    private final String exceptionCode;
    private final String exceptionDescription;

    CommonSystemExceptionEnum(String exceptionCode, String exceptionDescription) {
        this.exceptionCode = exceptionCode;
        this.exceptionDescription = exceptionDescription;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }
}
