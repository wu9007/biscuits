package org.hv.biscuit.constant;

/**
 * @author wujianchuan
 */

public enum OperateEnum {
    /**
     * 对数据的操作类型
     */
    ADD(Operate.ADD, "新建"),
    EDIT(Operate.EDIT, "编辑"),
    DELETE(Operate.DELETE, "删除"),
    DETAIL(Operate.DETAIL, "查看"),
    AUDIT(Operate.AUDIT, "审核"),
    REVOKE(Operate.REVOKE, "撤销审核"),
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
