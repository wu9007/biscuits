package org.hv.biscuits.controller.exception_headler;

import org.hv.biscuits.controller.Body;
import org.hv.biscuits.controller.exception.BusinessException;
import org.hv.biscuits.controller.exception.CommonSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author leyan95
 */
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Body handler(Exception e) {
        e.printStackTrace();
        if (e instanceof CommonSystemException) {
            return Body.warning().exceptionCode(((CommonSystemException) e).getExceptionCode()).title("错误提示").message(e.getMessage());
        }
        if (e instanceof BusinessException) {
            return Body.error().exceptionCode(((BusinessException) e).getExceptionCode()).title("错误提示").message(e.getMessage());
        }
        return Body.error().title("错误提示").message(e.getMessage());
    }
}
