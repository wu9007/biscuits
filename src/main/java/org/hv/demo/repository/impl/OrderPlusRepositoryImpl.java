package org.hv.demo.repository.impl;

import org.hv.biscuits.repository.AbstractCommonRepository;
import org.hv.demo.model.Order;
import org.hv.demo.repository.OrderPlusRepository;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan
 */
@Component
public class OrderPlusRepositoryImpl extends AbstractCommonRepository<Order> implements OrderPlusRepository {
}
