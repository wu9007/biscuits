package org.hv.biscuit.spine.model.repository.impl;

import org.hv.biscuit.annotation.Service;
import org.hv.biscuit.repository.AbstractRepository;
import org.hv.biscuit.spine.model.Mapper;
import org.hv.biscuit.spine.model.repository.SpineMapperRepository;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "biscuit")
public class SpineMapperRepositoryImpl extends AbstractRepository implements SpineMapperRepository {
    @Override
    public Mapper findOne(String uuid) throws SQLException {
        return (Mapper) this.getSession().findOne(Mapper.class, uuid);
    }
}
