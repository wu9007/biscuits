package org.hv.biscuit.spine.model.repository;

import org.hv.biscuit.repository.Repository;
import org.hv.biscuit.spine.model.Bundle;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface SpineBundleRepository extends Repository {

    Bundle findOne(String uuid) throws SQLException;
}
