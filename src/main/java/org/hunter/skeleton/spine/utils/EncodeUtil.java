package org.hunter.skeleton.spine.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wujianchuan 2019/2/15
 */
@Component
public class EncodeUtil {
    public String encoderByMd5(String code) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            return Base64.encodeBase64String(md5.digest(code.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
