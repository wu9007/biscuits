package org.hv.biscuit.spine.model.repository;

import org.hv.biscuit.repository.Repository;
import org.hv.biscuit.spine.model.Authority;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
public interface SpineAuthorityRepository extends Repository {
    Authority findOne(String uuid) throws SQLException;
}
