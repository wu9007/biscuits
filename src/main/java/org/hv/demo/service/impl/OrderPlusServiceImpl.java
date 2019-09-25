package org.hv.demo.service.impl;

import org.hv.biscuit.annotation.Affairs;
import org.hv.biscuit.annotation.Service;
import org.hv.biscuit.controller.FilterView;
import org.hv.biscuit.even.EvenCenter;
import org.hv.biscuit.service.AbstractService;
import org.hv.biscuit.service.PageList;
import org.hv.demo.DemoMediator;
import org.hv.demo.model.Order;
import org.hv.demo.repository.OrderPlusRepository;
import org.hv.demo.service.OrderPlusService;
import org.hv.demo.service.RelevantBillService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

import static org.hv.demo.constant.DemoEvenConstant.EVEN_ORDER_SAVE;
import static org.hv.demo.constant.DemoEvenConstant.EVEN_ORDER_UPDATE;
import static org.hv.demo.constant.DemoMediatorConstant.MEDIATOR_UPDATE_RELEVANT_STATUS;

/**
 * @author wujianchuan
 */
@Service(session = "demo")
public class OrderPlusServiceImpl extends AbstractService implements OrderPlusService {

    private final OrderPlusRepository orderRepository;
    private final RelevantBillService relevantBillService;
    private final DemoMediator demoMediator;

    @Autowired
    public OrderPlusServiceImpl(OrderPlusRepository orderRepository, DemoMediator demoMediator, RelevantBillService relevantBillService) {
        this.orderRepository = orderRepository;
        this.demoMediator = demoMediator;
        this.relevantBillService = relevantBillService;
    }

    @Override
    @Affairs
    public int save(Order order) throws Exception {
        // Call Mediator to check and change relevant bill status.
        this.demoMediator.call(MEDIATOR_UPDATE_RELEVANT_STATUS, order.getRelevantBillUuid(), false);

        // When transactions are nested, only the outermost transactions are opened.
        this.relevantBillService.findOne(order.getRelevantBillUuid());

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
