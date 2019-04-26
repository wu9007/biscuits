package org.hunter.demo.service.impl;

import org.hunter.demo.model.Order;
import org.hunter.demo.repository.OrderRepository;
import org.hunter.demo.service.OrderService;
import org.hunter.skeleton.annotation.Affairs;
import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
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
        this.injectRepository(this.orderRepository);
    }

    @Override
    @Affairs
    public int updateType(String type) throws SQLException {
        Order order = this.orderRepository.findOne("1011010");
        return this.orderRepository.updateOrder(order, type, "ADMIN");
    }
}
