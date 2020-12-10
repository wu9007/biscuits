package org.hv.biscuits.controller.exception;

/**
 * @author wujianchuan 2020/9/28 10:36
 */
public class CommonSystemException extends SystemException {
    private CommonSystemExceptionEnum commonSystemExceptionEnum;

    /**
     * 系统异常构造方法
     *
     * @param message 异常信息
     */
    public CommonSystemException(String message) {
        super(message);
    }

    /**
     * 系统异常构造方法
     *
     * @param commonSystemExceptionEnum 异常类型
     */
    public CommonSystemException(CommonSystemExceptionEnum commonSystemExceptionEnum) {
        super(commonSystemExceptionEnum.getExceptionDescription());
        this.commonSystemExceptionEnum = commonSystemExceptionEnum;
    }

    /**
     * 系统异常构造方法
     *
     * @param commonSystemExceptionEnum 异常类型
     * @param v1                  异常参数1
     */
    public CommonSystemException(CommonSystemExceptionEnum commonSystemExceptionEnum, String v1) {
        super(String.format(commonSystemExceptionEnum.getExceptionDescription(), v1));
        this.commonSystemExceptionEnum = commonSystemExceptionEnum;
    }

    /**
     * 获取一场编码
     *
     * @return 异常编码
     */
    @Override
    public String getExceptionCode() {
        if (commonSystemExceptionEnum == null) {
            return "E200000";
        }
        return commonSystemExceptionEnum.getExceptionCode();
    }
}
