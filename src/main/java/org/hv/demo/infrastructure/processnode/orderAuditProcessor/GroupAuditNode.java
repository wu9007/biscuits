package org.hv.demo.infrastructure.processnode.orderAuditProcessor;

import org.hv.biscuits.domain.process.State;
import org.hv.demo.bundles.bundle_order.service.OrderService;

/**
 * @author wujianchuan
 */
@State(group = "orderAuditProcessor")
public class GroupAuditNode extends AbstractOrderAuditNode {

    public GroupAuditNode(OrderService orderPlusService) {
        super(orderPlusService);
    }

    @Override
    public String getIdentify() {
        return "groupAuditNode";
    }

    @Override
    public String getSurName() {
        return "小组操作节点";
    }

    @Override
    public boolean doAccept(String dataUuid) {
        // do something ...
        System.out.println(String.format("单据：%s, 小组审核通过 进入下一个节点: %s", dataUuid, this.getNextNode().getSurName()));
        return true;
    }

    @Override
    public boolean doRejection(String dataUuid) {
        // do something ...
        System.out.println(String.format("单据：%s, 小组审核驳回 进入上一个节点: %s", dataUuid, this.getPreNode().getSurName()));
        return true;
    }
}
