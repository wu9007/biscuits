package org.hv.biscuits.core.views;

import java.io.Serializable;

/**
 * @author leyan95
 */
public class PermissionView implements Serializable {
    private static final long serialVersionUID = -3750247801987645175L;
    private String id;
    private String bundleId;
    private String name;
    private String description;

    public PermissionView() {
    }

    private PermissionView(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static PermissionView newInstance(String id, String name, String description) {
        return new PermissionView(id, name, description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
