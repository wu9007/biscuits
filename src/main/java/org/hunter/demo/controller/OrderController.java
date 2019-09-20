package org.hunter.demo.controller;

import org.hunter.demo.model.Order;
import org.hunter.demo.service.OrderPlusService;
import org.hunter.skeleton.annotation.Action;
import org.hunter.skeleton.annotation.Auth;
import org.hunter.skeleton.annotation.Controller;
import org.hunter.skeleton.controller.AbstractController;
import org.hunter.skeleton.controller.Body;
import org.hunter.skeleton.service.PageList;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "order")
public class OrderController extends AbstractController {
    private final OrderPlusService orderPlusService;

    public OrderController(OrderPlusService orderPlusService) {
        this.orderPlusService = orderPlusService;
    }

    @Auth(value = "order_read")
    @Action(actionId = "list_order", method = RequestMethod.GET)
    public Body<PageList> listOrder() throws Exception {
        PageList orders = this.orderPlusService.loadPageList(null);
        return Body.newSuccessInstance(orders);
    }

    @Auth(value = "order_manage")
    @Action(actionId = "save", method = RequestMethod.POST)
    public Body<String> saveOrder() throws Exception {
        Order order = Order.newInstance("DEMO-" + System.currentTimeMillis(), new BigDecimal(1.1));
        order.setRelevantBillUuid("10130");
        this.orderPlusService.save(order);
        return Body.newSuccessInstance("o o o o ok!");
    }
}
