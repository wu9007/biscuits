package org.hv.demo.bundles.bundle_relevant.service;

import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.service.PageList;
import org.hv.demo.bundles.bundle_relevant.aggregate.RelevantBill;
import org.hv.demo.bundles.bundle_relevant.repository.RelevantBillRepository;
import org.hv.demo.infrastructure.constant.DemoEvenConstant;

import java.sql.SQLException;

/**
 * @author wujianchuan
 * 监听类同服务类组合使用是  前提：{RelevantBillService extends Service, Monitor}
 */
@Service(session = "demo")
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
        return this.relevantBillRepository.findOne(uuid);
    }

    @Override
    public PageList loadPageList(FilterView filterView) throws SQLException {
        return this.relevantBillRepository.loadPage(filterView);
    }

    @Override
    public int updateStatus(String uuid, boolean newStatus) throws SQLException {
        return this.relevantBillRepository.updateStatus(uuid, newStatus);
    }

    @Override
    public String[] evenSourceIds() {
        return new String[]{DemoEvenConstant.EVEN_ORDER_SAVE, DemoEvenConstant.EVEN_ORDER_UPDATE};
    }

    @Override
    public void execute(Object... args) throws SQLException {
        System.out.println(String.format("I'm %s I can hear you.", this.getClass().getName()));
        System.out.println(String.format("Give me %s biscuit", args[0]));
        RelevantBill relevantBill = this.findOne("10130");
        System.out.println(String.format("Relevant Bill Code Is: %s", relevantBill.getCode()));
    }
}
