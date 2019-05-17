package org.hunter.demo.service.impl;

import org.hunter.demo.model.Order;
import org.hunter.demo.repository.OrderRepository;
import org.hunter.demo.service.OrderService;
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
public class OrderServiceImpl extends AbstractService implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Affairs
    public int save(Order order) throws SQLException, IllegalAccessException {
        return this.orderRepository.save(order, "ADMIN");
    }

    @Override
    @Affairs
    public int update(Order order) throws SQLException, IllegalAccessException {
        return this.orderRepository.update(order, "ADMIN");
    }

    @Override
    public Order findOne(String uuid) throws SQLException {
        return this.orderRepository.findOne(uuid);
    }

    @Override
    public PageList loadPageList(FilterView filterView) throws SQLException {
        return this.orderRepository.loadPage(filterView);
    }
}
