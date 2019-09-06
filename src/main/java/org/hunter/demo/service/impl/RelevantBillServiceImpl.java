package org.hunter.demo.service.impl;

import org.hunter.demo.model.RelevantBill;
import org.hunter.demo.repository.RelevantBillRepository;
import org.hunter.demo.service.RelevantBillService;
import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.service.PageList;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Service(session = "skeleton")
public class RelevantBillServiceImpl extends AbstractService implements RelevantBillService {
    private final
    RelevantBillRepository relevantBillRepository;

    public RelevantBillServiceImpl(RelevantBillRepository relevantBillRepository) {
        this.relevantBillRepository = relevantBillRepository;
    }

    @Override
    public int save(RelevantBill relevantBill) throws SQLException, IllegalAccessException {
        return this.relevantBillRepository.saveWithTrack(relevantBill, true, "ADMIN", "保存关联单据");
    }

    @Override
    public int update(RelevantBill relevantBill) throws SQLException, IllegalAccessException {
        return this.relevantBillRepository.updateWithTrack(relevantBill, true, "ADMIN", "更新关联单据");
    }

    @Override
    public RelevantBill findOne(String uuid) throws SQLException {
        return (RelevantBill) this.relevantBillRepository.findOne(uuid);
    }

    @Override
    public PageList loadPageList(FilterView filterView) throws SQLException {
        return this.relevantBillRepository.loadPage(filterView);
    }

    @Override
    public int updateStatus(String uuid, boolean newStatus) throws SQLException {
        return this.relevantBillRepository.updateStatus(uuid, newStatus);
    }
}
