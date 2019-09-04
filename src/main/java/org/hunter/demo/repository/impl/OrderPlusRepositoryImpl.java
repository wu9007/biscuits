package org.hunter.demo.repository.impl;

import org.hunter.demo.model.Order;
import org.hunter.demo.repository.OrderPlusRepository;
import org.hunter.demo.repository.OrderRepository;
import org.hunter.pocket.criteria.Criteria;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.repository.CommonRepository;
import org.hunter.skeleton.service.PageList;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
@Component
public class OrderPlusRepositoryImpl extends CommonRepository<Order> implements OrderPlusRepository {
}
