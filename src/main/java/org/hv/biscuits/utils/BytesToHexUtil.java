package org.hv.biscuits.utils;

/**
 * @author leyan95
 */
public class BytesToHexUtil {

	public static String fromBytesToHex(byte[] resultBytes){
		StringBuilder builder = new StringBuilder();
		for (byte resultByte : resultBytes) {
			if (Integer.toHexString(0xFF & resultByte).length() == 1) {
				builder.append("0").append(Integer.toHexString(0xFF & resultByte));
			} else {
				builder.append(Integer.toHexString(0xFF & resultByte));
			}
		}
		return builder.toString();
	}
}
