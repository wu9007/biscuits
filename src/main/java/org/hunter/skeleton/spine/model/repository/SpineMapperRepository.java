package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Mapper;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
public interface SpineMapperRepository extends Repository {
    Mapper findOne(String uuid) throws SQLException;
}
