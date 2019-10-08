package org.hv.demo.repository.impl;

import org.hv.biscuits.repository.AbstractCommonRepository;
import org.hv.demo.model.RelevantBill;
import org.hv.demo.repository.RelevantBillRepository;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Modern;
import org.hv.pocket.criteria.Restrictions;
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
