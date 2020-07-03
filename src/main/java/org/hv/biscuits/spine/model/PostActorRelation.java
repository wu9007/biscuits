package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;

/**
 * @author leyan95 2020/6/26
 */
@Entity(table = "T_POST_ACTOR", businessName = "岗位分工关联")
public class PostActorRelation extends AbstractBisEntity {
    private static final long serialVersionUID = -1878385856874316067L;
    @Column
    private String postUuid;
    @Column
    private String actorUuid;

    public PostActorRelation() {
    }

    public PostActorRelation(String postUuid, String actorUuid) {
        this.postUuid = postUuid;
        this.actorUuid = actorUuid;
    }

    public String getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(String postUuid) {
        this.postUuid = postUuid;
    }

    public String getActorUuid() {
        return actorUuid;
    }

    public void setActorUuid(String actorUuid) {
        this.actorUuid = actorUuid;
    }
}
