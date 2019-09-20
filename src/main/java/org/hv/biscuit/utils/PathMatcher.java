package org.hv.biscuit.utils;

/**
 * @author wujianchuan
 */
public class PathMatcher {
    private static final String ALL = "/*";

    public boolean matching(String reg, String input) {
        if (ALL.equals(reg)) {
            return true;
        }
        String[] regSplit = reg.split("\\*");
        int index = 0, regLen = regSplit.length;
        boolean b = reg.charAt(reg.length() - 1) == '*';
        while (input.length() > 0) {
            if (index == regLen) {
                return b;
            }
            String r = regSplit[index++];
            int indexOf = input.indexOf(r);
            if (indexOf == -1) {
                return false;
            }
            input = input.substring(indexOf + r.length());
        }
        return true;
    }
}
