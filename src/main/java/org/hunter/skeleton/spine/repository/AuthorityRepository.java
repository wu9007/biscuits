package org.hunter.skeleton.spine.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Authority;

/**
 * @author wujianchuan 2019/2/21
 */
public interface AuthorityRepository extends Repository {
    Authority findOne(long uuid);

}
