package org.hv.biscuits.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.controller.Body;
import org.hv.biscuits.controller.security.utils.AesUtil;
import org.hv.biscuits.controller.security.utils.ClientRsaUtil;
import org.hv.biscuits.utils.CommonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan 2020/3/4
 * @version 1.0
 * 依据注解添加加密标记
 */
public abstract class AbstractEncryptAdvice implements ResponseBodyAdvice<Object> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String CLIENT_NAME = "clientname";
    private final ObjectMapper objectMapper = CommonObjectMapper.getInstance();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Action action = returnType.getMethodAnnotation(Action.class);
        return action != null && action.responseEncrypt();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null != body) {
            String clientName = request.getHeaders().getFirst(CLIENT_NAME);
            if (clientName == null) {
                logger.warn("HtpHeaders 缺失参数 {}", CLIENT_NAME);
                return body;
            }
            try {
                byte[] aesKey = AesUtil.generateKey();
                byte[] encryptContent = AesUtil.encryptAES(objectMapper.writeValueAsBytes(body), aesKey);
                String encryptKey = ClientRsaUtil.encrypt(Base64.getEncoder().encodeToString(aesKey), clientName);
                if (encryptKey == null) {
                    body = Body.error().message(String.format("找不到客户端 %s 的公钥", clientName));
                } else {
                    Map<String, Object> encryptBody = new HashMap<>(4);
                    encryptBody.put("aesKey", encryptKey);
                    encryptBody.put("encryptContent", Base64.getEncoder().encodeToString(encryptContent));
                    body = encryptBody;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return body;
    }
}
