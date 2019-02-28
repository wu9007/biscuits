package org.hunter.skeleton.spine.model;

import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.annotation.OneToMany;
import org.hunter.pocket.model.BaseEntity;
import org.hunter.skeleton.spine.model.repository.MapperRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/1/30
 */
@Entity(table = "TBL_AUTHORITY", tableId = 102)
public class Authority extends BaseEntity {
    private static final long serialVersionUID = 8811730322368476299L;
    @Column(name = "SERVER_ID")
    private String serverId;
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "COMMENT")
    private String comment;
    @OneToMany(clazz = AuthMapperRelation.class, name = "AUTH_UUID")
    private List<AuthMapperRelation> authMapperRelationList;
    @OneToMany(clazz = RoleAuthRelation.class, name = "AUTH_UUID")
    private List<RoleAuthRelation> roleAuthRelationList;

    public Authority() {
    }

    public Authority(String serverId, String id, String name, String comment) {
        this.serverId = serverId;
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<AuthMapperRelation> getAuthMapperRelationList() {
        return authMapperRelationList;
    }

    public void setAuthMapperRelationList(List<AuthMapperRelation> authMapperRelationList) {
        this.authMapperRelationList = authMapperRelationList;
    }

    public List<RoleAuthRelation> getRoleAuthRelationList() {
        return roleAuthRelationList;
    }

    public void setRoleAuthRelationList(List<RoleAuthRelation> roleAuthRelationList) {
        this.roleAuthRelationList = roleAuthRelationList;
    }

    public List<Mapper> getMappers(MapperRepository mapperRepository) {
        List<Mapper> mappers = new ArrayList<>();
        List<AuthMapperRelation> authMapperRelations = this.getAuthMapperRelationList();
        if (authMapperRelations != null && authMapperRelations.size() > 0) {
            mappers = authMapperRelations.stream()
                    .map(authMapperRelation -> mapperRepository.findOne(authMapperRelation.getMapperUuid()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return mappers;
    }
}
