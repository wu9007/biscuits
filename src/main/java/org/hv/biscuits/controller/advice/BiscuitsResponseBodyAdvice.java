package org.hv.biscuits.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.annotation.Action;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author wujianchuan 2020/3/4
 * @version 1.0
 * 依据注解添加加密标记
 */
@RestControllerAdvice
public class BiscuitsResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Action action = returnType.getMethodAnnotation(Action.class);
        return action != null && action.responseEncrypt();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null != body) {
            response.getHeaders().add("encrypt", "true");
        }
        return body;
    }
}
