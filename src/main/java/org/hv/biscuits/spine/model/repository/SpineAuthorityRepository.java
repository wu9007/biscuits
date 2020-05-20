package org.hv.biscuits.spine.model.repository;

import org.hv.biscuits.spine.model.Authority;

import java.sql.SQLException;

/**
 * @author leyan95 2019/2/21
 */
public interface SpineAuthorityRepository {
    Authority findOne(String uuid) throws SQLException;
}
