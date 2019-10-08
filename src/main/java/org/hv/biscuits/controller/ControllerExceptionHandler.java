package org.hv.biscuits.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wujianchuan
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String UNIQUE_KEY = "UK_";
    private static final String FOREIGN_KEY = "FK_";

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Body handler(Exception e) {
        e.printStackTrace();
        if (e.getMessage() == null) {
            return Body.newWaringInstance("失败", e.toString(), e.getStackTrace());
        }
        String message;
        if (e.getMessage().contains(UNIQUE_KEY)) {
            // SQLIntegrityConstraintViolationException
            message = "系统中存在与当前记录相同的数据，不允许重复提交。";
        } else if (e.getMessage().contains(FOREIGN_KEY)) {
            message = "您当前操作的数据已被其他业务应用，无法执行删除操作，请先撤销后续业务后再执行删除。";
        } else {
            message = e.getMessage();
        }
        return Body.newWaringInstance("失败", message, e.getStackTrace());
    }
}
