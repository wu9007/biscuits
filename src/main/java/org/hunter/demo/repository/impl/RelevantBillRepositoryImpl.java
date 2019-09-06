package org.hunter.demo.repository.impl;

import org.hunter.demo.model.RelevantBill;
import org.hunter.demo.repository.RelevantBillRepository;
import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Modern;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.repository.AbstractCommonRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Component
public class RelevantBillRepositoryImpl extends AbstractCommonRepository<RelevantBill> implements RelevantBillRepository {
    @Override
    public int updateStatus(String uuid, Boolean newStatus) throws SQLException {
        Criteria criteria = super.getSession().createCriteria(super.genericClazz);
        criteria.add(Modern.set("available", newStatus))
                .add(Restrictions.equ("available", !newStatus))
                .add(Restrictions.equ("uuid", uuid));
        return criteria.update();
    }
}
