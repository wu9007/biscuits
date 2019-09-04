package org.hunter.demo.repository.impl;

import org.hunter.demo.model.Order;
import org.hunter.demo.repository.OrderPlusRepository;
import org.hunter.skeleton.repository.AbstractCommonRepository;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan
 */
@Component
public class OrderPlusRepositoryImpl extends AbstractCommonRepository<Order> implements OrderPlusRepository {
}
