package org.hv.biscuits.controller.exception_headler;

import org.hv.biscuits.controller.Body;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author wujianchuan
 */
@ControllerAdvice
public class SqlIntegrityConstraintExceptionHandler {

    private static final String UNIQUE_KEY = "UK_";
    private static final String FOREIGN_KEY = "FK_";

    @ResponseBody
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Body handler(SQLIntegrityConstraintViolationException e) {
        e.printStackTrace();
        if (e.getMessage() == null) {
            return Body.warning().title("失败").message(e.toString());
        }
        String message;
        if (e.getMessage().contains(UNIQUE_KEY)) {
            message = "系统中存在与当前记录相同的数据，不允许重复提交。";
        } else if (e.getMessage().contains(FOREIGN_KEY)) {
            message = "您当前操作的数据已被其他业务应用，无法执行删除操作，请先撤销后续业务后再执行删除。";
        } else {
            message = e.getMessage();
        }
        return Body.warning().title("失败").message(message);
    }
}
