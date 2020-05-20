package org.hv.biscuits.spine.model.repository.impl;

import org.hv.biscuits.repository.AbstractRepository;
import org.hv.biscuits.spine.model.Bundle;
import org.hv.biscuits.spine.model.repository.SpineBundleRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * @author leyan95 2019/2/26
 * @version 1.0
 */
@Repository
public class SpineBundleRepositoryImpl extends AbstractRepository implements SpineBundleRepository {

    @Override
    public Bundle findOne(String uuid) throws SQLException {
        return (Bundle) this.getSession().findOne(Bundle.class, uuid);
    }
}
