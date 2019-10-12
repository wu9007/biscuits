package org.hv.demo.processnode;

import org.hv.biscuits.domain.process.AbstractNode;
import org.hv.biscuits.domain.process.Node;
import org.hv.biscuits.domain.process.Transfer;

/**
 * @author wujianchuan
 */
@Transfer(processorName = "orderAuditProcessor", nodeName = "departmentAuditNode")
public class DepartmentAuditNode extends AbstractNode {

    @Override
    public boolean doAccept() {
        // do something ...
        System.out.println("科室审核通过 进入下一个节点。");
        return true;
    }

    @Override
    public boolean doRejection() {
        // do something ...
        System.out.println("科室审核驳回 进入上一个节点。");
        return true;
    }
}
