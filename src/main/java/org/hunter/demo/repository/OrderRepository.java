package org.hunter.demo.repository;


import org.hunter.demo.model.Order;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.service.PageList;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface OrderRepository extends Repository {

    int save(Order order, String operator) throws SQLException, IllegalAccessException;

    int update(Order order, String operator) throws SQLException, IllegalAccessException;

    Order findOne(String uuid) throws SQLException;

    PageList loadPage(FilterView filterView) throws SQLException;
}
