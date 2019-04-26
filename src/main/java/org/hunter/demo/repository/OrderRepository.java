package org.hunter.demo.repository;


import org.hunter.demo.model.Commodity;
import org.hunter.demo.model.Order;
import org.hunter.skeleton.repository.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
public interface OrderRepository extends Repository {

    int save(Order order, String operator) throws SQLException, IllegalAccessException;

    int update(Order order, String operator) throws SQLException, IllegalAccessException;

    Order findOne(String uuid) throws SQLException;
}
