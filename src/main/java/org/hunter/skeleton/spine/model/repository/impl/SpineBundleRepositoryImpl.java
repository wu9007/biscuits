package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Bundle;
import org.hunter.skeleton.spine.model.repository.SpineBundleRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Repository
public class SpineBundleRepositoryImpl extends AbstractRepository implements SpineBundleRepository {

    @Override
    public Bundle findOne(String uuid) throws SQLException {
        return (Bundle) this.getSession().findOne(Bundle.class, uuid);
    }
}
