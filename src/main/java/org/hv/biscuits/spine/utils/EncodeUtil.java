package org.hv.biscuits.spine.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author leyan95 2019/2/15
 */
@Component
public class EncodeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncodeUtil.class);

    public String encoderByMd5(String code) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            return Base64.encodeBase64String(md5.digest(code.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static String encode32Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] digest = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte b : digest) {
                i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return "";
        }
    }

    public String abcEncoder(String code) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(code.getBytes(StandardCharsets.UTF_8));
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
