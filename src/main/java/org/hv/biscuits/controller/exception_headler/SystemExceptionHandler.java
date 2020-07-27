package org.hv.biscuits.controller.exception_headler;

import org.hv.biscuits.controller.Body;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author leyan95
 */
@ControllerAdvice
public class SystemExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Body handler(Exception e) {
        e.printStackTrace();
        if (e.getMessage() == null) {
            return Body.error().title("失败").message(e.toString());
        }
        return Body.error().title("失败").message(e.getMessage());
    }
}
