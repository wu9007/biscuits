package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.Bundle;

import java.util.List;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
public interface BundleService {

    List<Bundle> getNoAuthBundle();

    Bundle findOne(Long uuid);

    List<Bundle> findNoGrouping();
}
