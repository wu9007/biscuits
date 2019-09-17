package org.hunter.demo.service.impl;

import org.hunter.demo.mediator.MaterialMediator;
import org.hunter.demo.mediator.MaterialMediatorImpl;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan
 */
@Service(session = "skeleton")
public class OrderServicePlusImpl extends AbstractService implements OrderPlusService {

    private final OrderPlusRepository orderRepository;
    private final MaterialMediatorImpl materialMediator;

    @Autowired
    public OrderServicePlusImpl(OrderPlusRepository orderRepository, MaterialMediatorImpl materialMediator) {
        this.orderRepository = orderRepository;
        this.materialMediator = materialMediator;
    }

    @Override
    @Affairs
    public int save(Order order) throws Exception {
        // Call Mediator to check and change relevant bill status.
        Map<String, Object> mediatorArgs = new HashMap<>();
        mediatorArgs.putIfAbsent("uuid", order.getRelevantBillUuid());
        mediatorArgs.putIfAbsent("available", false);
        this.materialMediator.call(MaterialMediator.UPDATE_RELEVANT_BILL_STATUS, mediatorArgs);

        int numberOfRowsAffected = this.orderRepository.saveWithTrack(order, true, "ADMIN", "保存订单");

        // Call monitors
        EvenCenter.getInstance().fireEven("order_save", numberOfRowsAffected, order.getUuid());
        return numberOfRowsAffected;
    }

    @Override
    @Affairs
    public int update(Order order) throws Exception {
        int numberOfRowsAffected = this.orderRepository.updateWithTrack(order, true, "ADMIN", "更新订单");

        // Call monitors
        EvenCenter.getInstance().fireEven("order_update", numberOfRowsAffected, order.getUuid());
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
