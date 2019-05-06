package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.repository.SpineAuthorityRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
@Repository
public class SpineAuthorityRepositoryImpl extends AbstractRepository implements SpineAuthorityRepository {
    @Override
    public Authority findOne(String uuid) throws SQLException {
        return (Authority) this.getSession().findDirect(Authority.class, uuid);
    }
}
