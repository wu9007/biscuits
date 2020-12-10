package org.hv.biscuits.controller.exception;

/**
 * @author wujianchuan 2020/9/28 10:55
 */
public enum BusinessExceptionEnum {
    /**
     * 业务异常
     */
    ILLEGAL_BAR_CODE("E200001", "请扫描正确的条码。"),
    UNABLE_TO_FIND_LINK("E200002", "该条码对应的献血流程无【%s】环节，请确认。"),
    STATE_OF_SWITCH_DONOR("E200003", "编辑状态，无法切换献血者信息，请点击取消后重新扫码。"),
    NO_FOUND_INFO("E200004", "未查询到【%s】为【%s】的【%s】信息，请确认。"),
    UPDATE_FAILED("E200005", "更新【%s】信息失败，请确认。"),
    DELETE_FAILED("E200006", "删除【%s】信息失败，请确认。"),
    ALREADY_EXIST("E200007", "名称为【%s】的【%s】已存在，请重新录入。");
    private final String exceptionCode;
    private final String exceptionDescription;

    BusinessExceptionEnum(String exceptionCode, String exceptionDescription) {
        this.exceptionCode = exceptionCode;
        this.exceptionDescription = exceptionDescription;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

}
