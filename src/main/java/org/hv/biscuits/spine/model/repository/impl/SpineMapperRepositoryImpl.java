package org.hv.biscuits.spine.model.repository.impl;

import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.repository.AbstractRepository;
import org.hv.biscuits.spine.model.Mapper;
import org.hv.biscuits.spine.model.repository.SpineMapperRepository;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "biscuits")
public class SpineMapperRepositoryImpl extends AbstractRepository implements SpineMapperRepository {
    @Override
    public Mapper findOne(String uuid) throws SQLException {
        return (Mapper) this.getSession().findOne(Mapper.class, uuid);
    }
}
