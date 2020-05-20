package org.hv.biscuits.spine.model.repository.impl;

import org.hv.biscuits.repository.AbstractRepository;
import org.hv.biscuits.spine.model.Authority;
import org.hv.biscuits.spine.model.repository.SpineAuthorityRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * @author leyan95 2019/2/21
 */
@Repository
public class SpineAuthorityRepositoryImpl extends AbstractRepository implements SpineAuthorityRepository {
    @Override
    public Authority findOne(String uuid) throws SQLException {
        return (Authority) this.getSession().findDirect(Authority.class, uuid);
    }
}
