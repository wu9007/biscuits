package org.hv.biscuits.spine.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
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

    public String abcEncoder(String code) {
        MessageDigest md5 = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(code.getBytes(StandardCharsets.UTF_8));
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
