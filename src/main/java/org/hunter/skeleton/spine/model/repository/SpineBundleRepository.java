package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Bundle;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface SpineBundleRepository extends Repository {

    List<Bundle> findByAuthIf(Boolean auth);

    Bundle findOne(String uuid) throws SQLException;

    List<Bundle> findAll();
}
