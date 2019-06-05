package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Bundle;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface SpineBundleRepository extends Repository {

    Bundle findOne(String uuid) throws SQLException;
}
