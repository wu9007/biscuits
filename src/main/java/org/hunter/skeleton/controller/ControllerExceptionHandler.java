package org.hunter.skeleton.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wujianchuan
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Body handler(Exception e) {
        logger.error("捕获到Exception异常", e);
        return Body.newWaringInstance("服务器异常", e.getMessage(), e.getStackTrace());
    }
}
