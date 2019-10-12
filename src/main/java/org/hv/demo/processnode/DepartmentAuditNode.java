package org.hv.demo.processnode;

import org.hv.biscuits.domain.process.AbstractNode;
import org.hv.biscuits.domain.process.Transfer;

/**
 * @author wujianchuan
 */
@Transfer(processorName = "orderAuditProcessor", nodeName = "departmentAuditNode")
public class DepartmentAuditNode extends AbstractNode {

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
