package org.hv.demo.bundles.bundle_order.controller;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.Body;
import org.hv.biscuits.service.PageList;
import org.hv.demo.bundles.bundle_order.aggregate.Order;
import org.hv.demo.bundles.bundle_order.service.OrderService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author leyan95
 */
@Controller(bundleId = "order", name = "订单")
public class OrderController {
    private final OrderService orderPlusService;

    public OrderController(OrderService orderPlusService) {
        this.orderPlusService = orderPlusService;
    }

    @Action(actionId = "list_order", method = RequestMethod.GET, authId = "order_read")
    public Body listOrder() throws Exception {
        PageList<?> orders = this.orderPlusService.loadPageList(null);
        return Body.success().data(orders);
    }

    @Action(actionId = "save", method = RequestMethod.POST)
    public Body saveOrder() throws Exception {
        Order order = Order.newInstance("DEMO-" + System.currentTimeMillis(), new BigDecimal("1.1"));
        order.setRelevantBillUuid("10130");
        this.orderPlusService.save(order);
        return Body.success().data("o o o o ok!");
    }

    @Action(actionId = "audit", method = RequestMethod.POST)
    public Body audit(@RequestParam String uuid) throws Exception {
        this.orderPlusService.audit(uuid);
        return Body.success().data("审核成功");
    }

    @Action(actionId = "rejection", method = RequestMethod.POST)
    public Body rejection(@RequestParam String uuid) throws Exception {
        this.orderPlusService.rejectionPreviousNode(uuid);
        return Body.success().data("撤销审核成功。");
    }

    @Action(actionId = "rejection_to_initial", method = RequestMethod.POST)
    public Body rejectionToInitial(@RequestParam String uuid) throws Exception {
        this.orderPlusService.rejectionInitialNode(uuid);
        return Body.success().data("驳回成功。");
    }
}
