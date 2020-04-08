package org.hv.demo.bundles.bundle_relevant.repository;

import org.hv.biscuits.repository.CommonRepository;
import org.hv.demo.bundles.bundle_relevant.aggregate.RelevantBill;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface RelevantBillRepository extends CommonRepository<RelevantBill> {

    int updateStatus(String uuid, Boolean newStatus) throws SQLException;
}
