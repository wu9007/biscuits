package org.hv.demo.service;

import org.hv.biscuit.controller.FilterView;
import org.hv.biscuit.service.PageList;
import org.hv.biscuit.service.Service;
import org.hv.demo.model.Order;

import java.sql.SQLException;

public interface OrderPlusService extends Service {

    int save(Order order) throws Exception;

    int update(Order order) throws Exception;

    Order findOne(String uuid) throws SQLException;

    PageList loadPageList(FilterView filterView) throws SQLException;
}
