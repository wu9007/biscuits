package org.hunter.demo.service;

import org.hunter.demo.model.RelevantBill;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.even.Monitor;
import org.hunter.skeleton.service.PageList;
import org.hunter.skeleton.service.Service;

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
