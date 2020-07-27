package org.hv.biscuits.controller.security;

import org.hv.biscuits.controller.security.utils.ServiceRsaUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Base64;

/**
 * @author wujianchuan 2020/3/4
 * @version 1.0
 * 对请求体中的数据进行解密操作
 */
public abstract class AbstractDecryptAdvice implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                String encrypt = httpInputMessage.getHeaders().getFirst("encrypt");
                InputStream inputStream = httpInputMessage.getBody();
                if (Boolean.parseBoolean(encrypt)) {
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    String result = new String(bytes);
                    try {
                        return new ByteArrayInputStream(ServiceRsaUtil.decrypt(Base64.getDecoder().decode(result.getBytes())));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return inputStream;
                }

            }

            @Override
            public HttpHeaders getHeaders() {
                return httpInputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}
