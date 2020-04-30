package org.hv.biscuits.core.views;

/**
 * @author wujianchuan
 */
public class PermissionView {
    private final String id;
    private final String name;
    private final String comment;

    private PermissionView(String id, String name, String comment) {
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    public static PermissionView newInstance(String id, String name, String comment) {
        return new PermissionView(id, name, comment);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
