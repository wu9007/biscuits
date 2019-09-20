package org.hv.demo.service;

import org.hv.biscuit.controller.FilterView;
import org.hv.biscuit.even.Monitor;
import org.hv.biscuit.service.PageList;
import org.hv.biscuit.service.Service;
import org.hv.demo.model.RelevantBill;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface RelevantBillService extends Service, Monitor {
    int save(RelevantBill relevantBill) throws SQLException, IllegalAccessException;

    int update(RelevantBill relevantBill) throws SQLException, IllegalAccessException;

    RelevantBill findOne(String uuid) throws SQLException;

    PageList loadPageList(FilterView filterView) throws SQLException;

    int updateStatus(String uuid, boolean newStatus) throws SQLException;
}
