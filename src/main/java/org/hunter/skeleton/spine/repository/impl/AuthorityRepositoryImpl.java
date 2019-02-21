package org.hunter.skeleton.spine.repository.impl;

import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.repository.AuthorityRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wujianchuan 2019/2/21
 */
@Repository
public class AuthorityRepositoryImpl extends AbstractRepository implements AuthorityRepository {
    @Override
    public Authority findOne(long uuid) {
        return (Authority) this.session().findOne(Authority.class, uuid);
    }
}
