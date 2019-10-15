package org.hv.demo.processnode;

import org.hv.biscuits.domain.process.AbstractNode;
import org.hv.demo.model.Order;
import org.hv.demo.service.OrderPlusService;

/**
 * @author wujianchuan
 */
public abstract class AbstractOrderAuditNode extends AbstractNode {
    private OrderPlusService orderPlusService;

    public AbstractOrderAuditNode(OrderPlusService orderPlusService) {
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
