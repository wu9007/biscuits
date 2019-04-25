package org.hunter.demo.repository;


import org.hunter.demo.model.Order;
import org.hunter.skeleton.repository.Repository;

/**
 * @author wujianchuan
 */
public interface OrderRepository extends Repository {

    int updateOrder(Order order, String orderType, String operator);

    Order findOne(String uuid);
}
