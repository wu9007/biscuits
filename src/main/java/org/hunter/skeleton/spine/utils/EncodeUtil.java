package org.hunter.skeleton.spine.utils;

import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

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
            BASE64Encoder base64en = new BASE64Encoder();
            return base64en.encode(md5.digest(code.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkCodeByMd5(String modernCode, String oldCode) {
        return this.encoderByMd5(modernCode).equals(oldCode);
    }
}
