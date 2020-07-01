package org.hv.biscuits.spine.model;

import org.hv.biscuits.spine.AbstractBisEntity;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;

/**
 * @author leyan95 2020/6/26
 */
@Entity(table = "T_POST_USER")
public class PostUserRelation extends AbstractBisEntity {
    private static final long serialVersionUID = -1878385856874316067L;
    @Column
    private String postUuid;
    @Column
    private String userUuid;

    public PostUserRelation() {
    }

    public PostUserRelation(String postUuid, String userUuid) {
        this.postUuid = postUuid;
        this.userUuid = userUuid;
    }

    public String getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(String postUuid) {
        this.postUuid = postUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
}
