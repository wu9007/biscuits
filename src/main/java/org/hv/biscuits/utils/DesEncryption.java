package org.hv.biscuits.utils;

import io.jsonwebtoken.lang.Assert;
import org.hv.biscuits.remote.EncryptionComputing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * @author wujianchuan
 */
public class DesEncryption implements EncryptionComputing {

    private final byte[] desKey;

    private DesEncryption(byte[] desKey) {
        this.desKey = desKey;
    }

    public static DesEncryption newInstance(String secretKey) {
        Assert.notNull(secretKey);
        return new DesEncryption(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encrypt(String input) throws Exception {
        return base64Encode(desEncrypt(input.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public String decrypt(String input) throws Exception {
        byte[] result = base64Decode(input);
        return new String(desDecrypt(result), StandardCharsets.UTF_8);
    }

    private byte[] desEncrypt(byte[] plainText) throws Exception {
        DESKeySpec dks = new DESKeySpec(desKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new SecureRandom());
        return cipher.doFinal(plainText);
    }

    private byte[] desDecrypt(byte[] encryptText) throws Exception {
        DESKeySpec dks = new DESKeySpec(desKey);
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

    private static byte[] base64Decode(String s) throws IOException {
        if (s == null) {
            return null;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(s.replaceAll(" +", "+").replace("\r\n", ""));
    }
}
