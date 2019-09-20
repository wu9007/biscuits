package org.hv.demo.repository;

import org.hv.biscuit.repository.CommonRepository;
import org.hv.demo.model.RelevantBill;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface RelevantBillRepository extends CommonRepository<RelevantBill> {

    int updateStatus(String uuid, Boolean newStatus) throws SQLException;
}
