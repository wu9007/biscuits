package org.hv.biscuits.remote;

/**
 * @author leyan95
 */
public interface EncryptionComputing {

    /**
     * 加密
     *
     * @param input 待加密字符串
     * @return 加密后的字符串
     * @throws Exception e
     */
    String encrypt(String input) throws Exception;

    /**
     * 解密
     *
     * @param input 待解密字符串
     * @return 解密后的字符串
     * @throws Exception e
     */
    String decrypt(String input) throws Exception;
}
