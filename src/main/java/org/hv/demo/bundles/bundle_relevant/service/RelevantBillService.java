package org.hv.demo.bundles.bundle_relevant.service;

import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.domain.even.Monitor;
import org.hv.biscuits.service.PageList;
import org.hv.biscuits.service.Service;
import org.hv.demo.bundles.bundle_relevant.aggregate.RelevantBill;

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
