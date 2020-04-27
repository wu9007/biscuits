package org.hv.biscuits.spine.model.repository;

import org.hv.biscuits.spine.model.Mapper;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
public interface SpineMapperRepository {
    Mapper findOne(String uuid) throws SQLException;
}
