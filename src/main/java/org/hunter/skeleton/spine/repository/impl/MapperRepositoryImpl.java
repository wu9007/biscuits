package org.hunter.skeleton.spine.repository.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.spine.repository.MapperRepository;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "skeleton")
public class MapperRepositoryImpl extends AbstractRepository implements MapperRepository {
    @Override
    public Mapper findOne(long uuid) {
        return (Mapper) this.session().findOne(Mapper.class, uuid);
    }
}
