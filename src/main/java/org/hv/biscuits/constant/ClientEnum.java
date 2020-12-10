package org.hv.biscuits.constant;

/**
 * @author leyan95
 */

public enum ClientEnum {
    /**
     * 客户端
     */
    BROWSER("BROWSER", "浏览器"),
    PDA("PDA", "扫码枪"),
    EXTERNAL("EXTERNAL", "第三方"),
    ;
    private final String id;
    private final String name;

    ClientEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
