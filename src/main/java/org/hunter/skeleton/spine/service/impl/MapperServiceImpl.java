package org.hunter.skeleton.spine.service.impl;

import org.hunter.skeleton.annotation.Service;
import org.hunter.skeleton.service.AbstractService;
import org.hunter.skeleton.spine.model.Authority;
import org.hunter.skeleton.spine.model.Mapper;
import org.hunter.skeleton.spine.repository.MapperRepository;
import org.hunter.skeleton.spine.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/21
 */
@Service(session = "skeleton")
public class MapperServiceImpl extends AbstractService implements MapperService {
    private final MapperRepository mapperRepository;

    @Autowired
    public MapperServiceImpl(MapperRepository mapperRepository) {
        this.mapperRepository = mapperRepository;
        this.mapperRepository.setSession(this.sessionThreadLocal);
    }

    @Override
    public List<Mapper> loadByAuthority(Authority authority) {
        return authority.getAuthMapperRelationList().stream()
                .map(authMapperRelation -> this.mapperRepository.findOne(authMapperRelation.getMapperUuid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
