package org.hv.demo.processnode;

import org.hv.biscuits.domain.process.State;
import org.hv.demo.service.OrderPlusService;

/**
 * @author wujianchuan
 */
@State(group = "orderAuditProcessor")
public class DepartmentAuditNode extends AbstractOrderAuditNode {

    public DepartmentAuditNode(OrderPlusService orderPlusService) {
        super(orderPlusService);
    }

    @Override
    public String getIdentify() {
        return "departmentAuditNode";
    }

    @Override
    public String getSurName() {
        return "科室操作节点";
    }

    @Override
    public boolean doAccept(String dataUuid) {
        // do something ...
        System.out.println(String.format("单据：%s, 科室审核通过 进入下一个节点: %s", dataUuid, this.getNextNode().getSurName()));
        return true;
    }

    @Override
    public boolean doRejection(String dataUuid) {
        // do something ...
        System.out.println(String.format("单据：%s, 科室审核驳回 进入上一个节点: %s", dataUuid, this.getPreNode().getSurName()));
        return true;
    }
}
