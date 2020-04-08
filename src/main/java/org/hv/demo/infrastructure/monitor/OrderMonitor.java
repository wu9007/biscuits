package org.hv.demo.infrastructure.monitor;

import org.hv.biscuits.domain.even.Monitor;
import org.hv.demo.bundles.bundle_order.aggregate.Order;
import org.hv.demo.infrastructure.mediator.OrderMediator;
import org.springframework.stereotype.Component;

import static org.hv.demo.infrastructure.constant.DemoEvenConstant.EVEN_ORDER_SAVE;
import static org.hv.demo.infrastructure.constant.DemoEvenConstant.EVEN_ORDER_UPDATE;
import static org.hv.demo.infrastructure.constant.DemoMediatorConstant.MEDIATOR_FIND_ORDER_BY_UUID;

/**
 * @author wujianchuan
 * 监听类单独使用
 */
@Component
public class OrderMonitor implements Monitor {
    private final OrderMediator orderMediator;

    public OrderMonitor(OrderMediator orderMediator) {
        this.orderMediator = orderMediator;
    }

    @Override
    public String[] evenSourceIds() {
        return new String[]{EVEN_ORDER_SAVE, EVEN_ORDER_UPDATE};
    }

    @Override
    public void execute(Object... args) throws Exception {
        System.out.println(String.format("Catch even of order %s，My show time!!!", args[2]));
        System.out.println(String.format("Order uuid is: %s", args[1]));
        Order order = (Order) orderMediator.call(MEDIATOR_FIND_ORDER_BY_UUID, args[1]);
        System.out.println(String.format("Order code is: %s", order.getCode()));
        System.out.println(String.format("Number of rows affected is: %s", args[0]));
    }
}
