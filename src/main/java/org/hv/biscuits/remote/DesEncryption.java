package org.hv.biscuits.remote;


import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author wujianchuan
 */
public class DesEncryption implements EncryptionComputing {

    private byte[] desKey;

    private DesEncryption(byte[] desKey) {
        this.desKey = desKey;
    }

    public static DesEncryption newInstance(String secretKey) {
        Assert.notNull(secretKey, "secret key cannot be null.");
        return new DesEncryption(secretKey.getBytes());
    }

    @Override
    public String encrypt(String input) throws Exception {
        return base64Encode(desEncrypt(input.getBytes()));
    }

    @Override
    public String decrypt(String input) throws Exception {
        byte[] result = base64Decode(input);
        return new String(desDecrypt(result));
    }

    private byte[] desEncrypt(byte[] plainText) throws Exception {
        byte[] rawKeyData = desKey;
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new SecureRandom());
        return cipher.doFinal(plainText);
    }

    private byte[] desDecrypt(byte[] encryptText) throws Exception {
        byte[] rawKeyData = desKey;
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, new SecureRandom());
        return cipher.doFinal(encryptText);
    }

    private static String base64Encode(byte[] s) {
        if (s == null) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(s);
    }

    private static byte[] base64Decode(String s) {
        if (s == null) {
            return null;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(s.replaceAll(" +", "+").replace("\r\n", ""));
    }
}