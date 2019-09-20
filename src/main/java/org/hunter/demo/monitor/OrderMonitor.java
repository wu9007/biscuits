package org.hunter.demo.monitor;

import org.hunter.demo.DemoMediator;
import org.hunter.demo.model.Order;
import org.hunter.skeleton.even.Monitor;
import org.springframework.stereotype.Component;

import static org.hunter.demo.constant.DemoEvenConstant.EVEN_ORDER_SAVE;
import static org.hunter.demo.constant.DemoEvenConstant.EVEN_ORDER_UPDATE;
import static org.hunter.demo.constant.DemoMediatorConstant.MEDIATOR_FIND_ORDER_BY_UUID;

/**
 * @author wujianchuan
 * 监听类单独使用
 */
@Component
public class OrderMonitor implements Monitor {
    private final DemoMediator demoMediator;

    public OrderMonitor(DemoMediator demoMediator) {
        this.demoMediator = demoMediator;
    }

    @Override
    public String[] evenSourceIds() {
        return new String[]{EVEN_ORDER_SAVE, EVEN_ORDER_UPDATE};
    }

    @Override
    public void execute(Object... args) throws Exception {
        System.out.println(String.format("Catch even of order %s，My show time!!!", args[2]));
        System.out.println(String.format("Order uuid is: %s", args[1]));
        Order order = (Order) demoMediator.call(MEDIATOR_FIND_ORDER_BY_UUID, args[1]);
        System.out.println(String.format("Order code is: %s", order.getCode()));
        System.out.println(String.format("Number of rows affected is: %s", args[0]));
    }
}
