package org.hv.demo.processnode;

import org.hv.biscuits.domain.process.AbstractNode;
import org.hv.biscuits.domain.process.Transfer;

/**
 * @author wujianchuan
 */
@Transfer(processorName = "orderAuditProcessor", nodeName = "directLeaderAuditNode")
public class DirectLeaderAuditNode extends AbstractNode {
    @Override
    public boolean doAccept() {
        // do something ...
        System.out.println("直管领导审核通过 进入下一个节点。");
        return true;
    }

    @Override
    public boolean doRejection() {
        // do something ...
        System.out.println("直管领导审核驳回 进入上一个节点。");
        return true;
    }
}
