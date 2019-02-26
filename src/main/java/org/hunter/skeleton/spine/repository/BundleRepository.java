package org.hunter.skeleton.spine.repository;

import org.hunter.skeleton.repository.Repository;
import org.hunter.skeleton.spine.model.Bundle;

import java.util.List;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface BundleRepository extends Repository {

    List<Bundle> findByAuthIf(Boolean auth);
}
