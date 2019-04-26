package org.hunter.skeleton.spine.model.repository;

import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.BundleGroup;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan 2019/2/27
 * @version 1.0
 */
public interface BundleGroupRepository extends Repository {

    BundleGroup findOne(String uuid) throws SQLException;

    List<BundleGroup> findAll(FilterView filterView);

    int save(BundleGroup bundleGroup) throws SQLException;

    int delete(BundleGroup bundleGroup) throws SQLException, IllegalAccessException;

    int update(BundleGroup bundleGroup) throws SQLException;
}
