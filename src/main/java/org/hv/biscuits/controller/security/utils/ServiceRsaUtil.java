package org.hv.biscuits.controller.security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leyan95
 */
public class ServiceRsaUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRsaUtil.class);

    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final Map<String, Object> KEY_MAP = new HashMap<>();

    /*
      生成RSA的公钥和私钥
     */
    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            KEY_MAP.put(PUBLIC_KEY, publicKey);
            KEY_MAP.put(PRIVATE_KEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 公钥加密
     */
    public static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, (RSAPublicKey) KEY_MAP.get(PUBLIC_KEY));
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     */
    public static byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, (RSAPrivateKey) KEY_MAP.get(PRIVATE_KEY));
        return cipher.doFinal(data);
    }

    /**
     * 获得公钥
     */
    public static RSAPublicKey getPublicKey() {
        return (RSAPublicKey) KEY_MAP.get(PUBLIC_KEY);
    }

    /**
     * 获得私钥
     */
    public static RSAPrivateKey getPrivateKey() {
        return (RSAPrivateKey) KEY_MAP.get(PRIVATE_KEY);
    }
}
