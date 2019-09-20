package org.hunter.demo.service;

import org.hunter.demo.model.Order;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.PageList;
import org.hunter.skeleton.service.Service;

import java.sql.SQLException;

public interface OrderPlusService extends Service {

    int save(Order order) throws Exception;

    int update(Order order) throws Exception;

    Order findOne(String uuid) throws SQLException;

    PageList loadPageList(FilterView filterView) throws SQLException;
}
