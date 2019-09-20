package org.hunter.demo.service.impl;

import org.hunter.demo.DemoMediator;
import org.hunter.demo.constant.DemoMediatorConstant;
import org.hunter.demo.model.Order;
import org.hunter.demo.repository.OrderPlusRepository;
import org.hunter.demo.service.OrderPlusService;
import org.hunter.skeleton.annotation.Affairs;
import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.even.EvenCenter;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.service.PageList;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

import static org.hunter.demo.constant.DemoEvenConstant.EVEN_ORDER_SAVE;
import static org.hunter.demo.constant.DemoEvenConstant.EVEN_ORDER_UPDATE;


/**
 * @author wujianchuan
 */
@Service(session = "demo")
public class OrderPlusServiceImpl extends AbstractService implements OrderPlusService {

    private final OrderPlusRepository orderRepository;
    private final DemoMediator demoMediator;

    @Autowired
    public OrderPlusServiceImpl(OrderPlusRepository orderRepository, DemoMediator demoMediator) {
        this.orderRepository = orderRepository;
        this.demoMediator = demoMediator;
    }

    @Override
    @Affairs
    public int save(Order order) throws Exception {
        // Call Mediator to check and change relevant bill status.
        this.demoMediator.call(DemoMediatorConstant.MEDIATOR_UPDATE_RELEVANT_STATUS, order.getRelevantBillUuid(), false);

        // Execute own job
        int numberOfRowsAffected = this.orderRepository.saveWithTrack(order, true, "ADMIN", "保存订单");

        // Call monitors
        EvenCenter.getInstance().fireEven(EVEN_ORDER_SAVE, numberOfRowsAffected, order.getUuid(), "save");
        return numberOfRowsAffected;
    }

    @Override
    @Affairs
    public int update(Order order) throws Exception {
        int numberOfRowsAffected = this.orderRepository.updateWithTrack(order, true, "ADMIN", "更新订单");

        // Call monitors
        EvenCenter.getInstance().fireEven(EVEN_ORDER_UPDATE, numberOfRowsAffected, order.getUuid(), "update");
        return numberOfRowsAffected;
    }

    @Override
    public Order findOne(String uuid) throws SQLException {
        return (Order) this.orderRepository.findOne(uuid);
    }

    @Override
    public PageList loadPageList(FilterView filterView) throws SQLException {
        return this.orderRepository.loadPage(filterView);
    }
}
