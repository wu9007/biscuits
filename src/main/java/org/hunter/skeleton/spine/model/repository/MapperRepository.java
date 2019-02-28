package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Mapper;

import java.util.List;

/**
 * @author wujianchuan 2019/2/21
 */
public interface MapperRepository extends Repository {
    Mapper findOne(long uuid);

    List<Mapper> findByBundle(Long bundleUuid);
}
