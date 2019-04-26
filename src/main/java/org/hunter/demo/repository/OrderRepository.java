package org.hunter.demo.repository;


import org.hunter.demo.model.Order;
import org.hunter.skeleton.repository.Repository;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface OrderRepository extends Repository {

    int updateOrder(Order order, String orderType, String operator) throws SQLException;

    Order findOne(String uuid) throws SQLException;
}
