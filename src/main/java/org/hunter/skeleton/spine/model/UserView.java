package org.hunter.skeleton.spine.model;

/**
 * @author wujianchuan 2019/2/25
 */
public class UserView {
    private String avatar;
    private String name;
    private String phone;
    private String memo;
    private Integer sort;

    UserView(String avatar, String name, String phone, String memo, Integer sort) {
        this.avatar = avatar;
        this.name = name;
        this.phone = phone;
        this.memo = memo;
        this.sort = sort;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
