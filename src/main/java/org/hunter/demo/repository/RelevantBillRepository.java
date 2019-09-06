package org.hunter.demo.repository;

import org.hunter.demo.model.RelevantBill;
import org.hunter.skeleton.repository.CommonRepository;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface RelevantBillRepository extends CommonRepository<RelevantBill> {

    int updateStatus(String uuid, Boolean newStatus) throws SQLException;
}
