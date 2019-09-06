package org.hunter.demo.service;

import org.hunter.demo.model.Order;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.PageList;

import java.sql.SQLException;

public interface OrderPlusService {

    int save(Order order) throws Exception;

    int update(Order order) throws SQLException, IllegalAccessException;

    Order findOne(String uuid) throws SQLException;

    PageList loadPageList(FilterView filterView) throws SQLException;
}
