package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Mapper;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan 2019/2/21
 */
public interface SpineMapperRepository extends Repository {
    Mapper findOne(String uuid) throws SQLException;

    List<Mapper> findByBundle(String bundleUuid);
}
