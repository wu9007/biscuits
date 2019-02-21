package org.hunter.skeleton.spine.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Mapper;

/**
 * @author wujianchuan 2019/2/21
 */
public interface MapperRepository extends Repository {
    Mapper findOne(long uuid);
}
