package org.hv.biscuits.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.util.Map;

/**
 * @author wujianchuan 2020/3/4
 * @version 1.0
 */
@RestControllerAdvice
public class BiscuitsResponseBodyAdvice implements ResponseBodyAdvice {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null != body) {
            try {
                Map map = objectMapper.readValue(objectMapper.writeValueAsString(body), Map.class);
                map.forEach((key, value) -> map.put(key, value + "-encrypt"));
                return map;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return body;
    }
}
