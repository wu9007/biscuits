package org.hv.demo.processnode;

import org.hv.biscuits.domain.process.AbstractNode;
import org.hv.biscuits.domain.process.Transfer;

/**
 * @author wujianchuan
 */
@Transfer(processorName = "orderAuditProcessor", nodeName = "directLeaderAuditNode")
public class DirectLeaderAuditNode extends AbstractNode {
    @Override
    public boolean doAccept(String dataUuid) {
        // do something ...
        System.out.println(String.format("单据：%s, 直属领导审核通过 进入下一个节点。", dataUuid));
        return true;
    }

    @Override
    public boolean doRejection(String dataUuid) {
        // do something ...
        System.out.println(String.format("单据：%s, 直属领导审核驳回 进入上一个节点。", dataUuid));
        return true;
    }
}
