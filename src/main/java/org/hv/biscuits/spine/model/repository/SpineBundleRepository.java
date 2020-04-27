package org.hv.biscuits.spine.model.repository;

import org.hv.biscuits.spine.model.Bundle;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface SpineBundleRepository {

    Bundle findOne(String uuid) throws SQLException;
}
