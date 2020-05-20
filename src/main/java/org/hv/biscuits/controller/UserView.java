package org.hv.biscuits.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author leyan95
 */
public class UserView {
    private String uuid;
    private String avatar;
    private String name;
    private String departmentUuid;
    private String departmentName;
    private List authIds;
    private Set bundleIds;

    public String getUuid() {
        return uuid;
    }

    public UserView setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public static UserView newInstance() {
        return new UserView();
    }

    public static UserView formMap(Map<String, String> map) {
        return newInstance();
    }

    public String getAvatar() {
        return avatar;
    }

    public UserView setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserView setName(String name) {
        this.name = name;
        return this;
    }

    public String getDepartmentUuid() {
        return departmentUuid;
    }

    public UserView setDepartmentUuid(String departmentUuid) {
        this.departmentUuid = departmentUuid;
        return this;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public UserView setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public List getAuthIds() {
        return authIds;
    }

    public UserView setAuthIds(List authIds) {
        this.authIds = authIds;
        return this;
    }

    public Set getBundleIds() {
        return bundleIds;
    }

    public UserView setBundleIds(Set bundleIds) {
        this.bundleIds = bundleIds;
        return this;
    }
}
