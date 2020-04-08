package org.hv.demo.infrastructure.processnode.orderAuditProcessor;

import org.hv.biscuits.domain.process.AbstractNode;
import org.hv.demo.bundles.bundle_order.aggregate.Order;
import org.hv.demo.bundles.bundle_order.service.OrderService;

/**
 * @author wujianchuan
 */
public abstract class AbstractOrderAuditNode extends AbstractNode {
    private OrderService orderPlusService;

    public AbstractOrderAuditNode(OrderService orderPlusService) {
        this.orderPlusService = orderPlusService;
    }

    @Override
    public boolean doRejectionToInitial(String dataUuid) throws Exception {
        Order order = this.orderPlusService.findOne(dataUuid);
        order.setState(false);
        this.orderPlusService.update(order);
        System.out.println(String.format("订单 - %s，被驳回到初始状态。", order.getCode()));
        return true;
    }
}
