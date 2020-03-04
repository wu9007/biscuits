package org.hv.biscuits.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.utils.AesUtil;
import org.hv.biscuits.utils.BytesToHexUtil;
import org.hv.biscuits.utils.RsaUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan 2020/3/4
 * @version 1.0
 * 将响应体里面的data进行加密（AES加密数据，RSA加密AES的密钥）
 */
//@RestControllerAdvice
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
                Map<String, Object> map = objectMapper.readValue(objectMapper.writeValueAsString(body), Map.class);
                if (map.get("success") != null && (Boolean) map.get("success")) {
                    Object data = map.get("data");
                    if (data != null) {
                        byte[] aesKey = AesUtil.initKey();
                        byte[] encodeAesKeyByte = RsaUtil.encrypt(aesKey);
                        String encodeAesKeyStr = BytesToHexUtil.fromBytesToHex(encodeAesKeyByte);
                        map.put("encodeAesKey", encodeAesKeyStr);
                        map.put("data", AesUtil.encryptAES(objectMapper.writeValueAsBytes(data), aesKey));
                    }
                }
                return map;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return body;
    }
}
