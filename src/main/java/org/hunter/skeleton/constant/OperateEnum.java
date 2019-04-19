package org.hunter.skeleton.constant;

/**
 * @author wujianchuan
 */

public enum OperateEnum {
    /**
     * 对数据的操作类型
     */
    ADD("add", "新建"),
    EDIT("edit", "编辑"),
    DELETE("delete", "删除"),
    DETAIL("detail", "查看"),
    AUDIT("audit", "审核"),
    REVOKE("revoke", "撤销审核"),
    ;
    private final String id;
    private final String name;

    OperateEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }}
