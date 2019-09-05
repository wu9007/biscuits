package org.hunter.demo.service.impl;

import org.hunter.demo.model.Order;
import org.hunter.demo.repository.OrderPlusRepository;
import org.hunter.demo.service.OrderPlusService;
import org.hunter.skeleton.annotation.Affairs;
import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.service.PageList;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Service(session = "skeleton")
public class OrderServicePlusImpl extends AbstractService implements OrderPlusService {

    private final OrderPlusRepository orderRepository;

    @Autowired
    public OrderServicePlusImpl(OrderPlusRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Affairs
    public int save(Order order) throws SQLException, IllegalAccessException {
        // TODO Check and Change Relevant Bill status.
        return this.orderRepository.saveWithTrack(order, true, "ADMIN", "保存订单");
    }

    @Override
    @Affairs
    public int update(Order order) throws SQLException, IllegalAccessException {
        return this.orderRepository.updateWithTrack(order, true, "ADMIN", "更新订单");
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
