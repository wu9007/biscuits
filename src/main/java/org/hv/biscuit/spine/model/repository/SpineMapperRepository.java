package org.hv.biscuit.spine.model.repository;

import org.hv.biscuit.repository.Repository;
import org.hv.biscuit.spine.model.Mapper;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
public interface SpineMapperRepository extends Repository {
    Mapper findOne(String uuid) throws SQLException;
}
