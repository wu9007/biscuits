package org.hv.biscuits.controller.exception;

/**
 * @author wujianchuan 2020/9/28 10:36
 */
public abstract class BusinessException extends Exception {

    /**
     * 系统异常构造方法
     *
     * @param message 异常信息
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 获取异常编码
     *
     * @return 异常编码
     */
    public abstract String getExceptionCode();
}
