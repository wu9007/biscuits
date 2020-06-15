package org.hv.biscuits.core.views;

/**
 * @author leyan95
 */
public class PermissionView {
    private final String id;
    private String bundleId;
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

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
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
