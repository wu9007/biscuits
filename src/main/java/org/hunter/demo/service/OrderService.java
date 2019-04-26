package org.hunter.demo.service;

import org.hunter.demo.model.Order;

import java.sql.SQLException;

public interface OrderService {

    int save(Order order) throws SQLException, IllegalAccessException;

    int update(Order order) throws SQLException, IllegalAccessException;

    Order findOne(String uuid) throws SQLException;
}
