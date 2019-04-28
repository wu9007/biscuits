package org.hunter.skeleton.spine.model.repository.impl;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.repository.AbstractRepository;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.spine.model.repository.MapperRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "skeleton")
public class MapperRepositoryImpl extends AbstractRepository implements MapperRepository {
    @Override
    public Mapper findOne(String uuid) throws SQLException {
        return (Mapper) this.getSession().findOne(Mapper.class, uuid);
    }

    @Override
    public List<Mapper> findByBundle(String bundleUuid) {
        Criteria criteria = this.getSession().createCriteria(Mapper.class);
        criteria.add(Restrictions.equ("bundleUuid", bundleUuid));
        return criteria.list();
    }
}
