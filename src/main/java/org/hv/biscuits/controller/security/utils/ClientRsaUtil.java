package org.hv.biscuits.controller.security.utils;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leyan95
 * @version 1.0
 */
public class ClientRsaUtil {

    private static final Map<String, Key> KEY_MAP = new ConcurrentHashMap<>();

    /**
     * 添加客户端公钥
     */
    public static void addClientPublicKey(String clientKey, String publicKeyStr) throws Exception {
        Key publicKey = buildPublicKey(publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", ""));
        KEY_MAP.putIfAbsent(clientKey, publicKey);
    }

    /**
     * 使用客户端公钥加密
     */
    public static String encrypt(String source, String clientName) throws Exception {
        Key key = KEY_MAP.get(clientName);
        if (key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(source.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static PublicKey buildPublicKey(String key) throws Exception {
        byte[] keyBytes;
//        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        keyBytes = java.util.Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey buildPrivateKey(String key) throws Exception {
        byte[] keyBytes;
//        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        keyBytes = java.util.Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
