package org.hv.demo.bundles.bundle_order.service;

import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.PageList;
import org.hv.biscuits.service.Service;
import org.hv.demo.bundles.bundle_order.aggregate.Order;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface OrderService extends Service {

    int save(Order order) throws Exception;

    int update(Order order) throws Exception;

    boolean audit(String uuid) throws Exception;

    boolean rejectionPreviousNode(String uuid) throws Exception;

    boolean rejectionInitialNode(String uuid) throws Exception;

    Order findOne(String uuid) throws SQLException;

    PageList loadPageList(FilterView filterView) throws SQLException;
}
