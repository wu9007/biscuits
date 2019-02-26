package org.hunter.skeleton.spine.service;

import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Bundle;
import org.hunter.skeleton.spine.model.Mapper;

import java.util.List;

/**
 * @author wujianchuan 2019/2/21
 */
public interface MapperService {

    List<Mapper> loadByAuthority(Authority authority);

    List<Mapper> loadByBundle(Long uuid);
}
