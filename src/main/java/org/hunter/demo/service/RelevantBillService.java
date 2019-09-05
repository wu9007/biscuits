package org.hunter.demo.service;

import org.hunter.demo.model.RelevantBill;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.PageList;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface RelevantBillService {
    int save(RelevantBill relevantBill) throws SQLException, IllegalAccessException;

    int update(RelevantBill relevantBill) throws SQLException, IllegalAccessException;

    RelevantBill findOne(String uuid) throws SQLException;

    PageList loadPageList(FilterView filterView) throws SQLException;
}
