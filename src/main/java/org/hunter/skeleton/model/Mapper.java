package org.hunter.skeleton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hunter.pocket.annotation.Column;
import org.hunter.pocket.annotation.Entity;
import org.hunter.pocket.model.BaseEntity;

/**
 * 路径映射实体类
 *
 * @author wujianchuan 2019/2/3
 */
@Entity(table = "TBL_MAPPER")
public class Mapper extends BaseEntity {
    @Column(name = "SERVER_NAME", businessName = "服务名")
    private String serverName;
    @Column(name = "REQUEST_METHOD", businessName = "请求方式")
    private String requestMethod;
    @Column(name = "ID", businessName = "映射")
    private String id;

    public Mapper() {
    }

    public Mapper(String serverName, String requestMethod, String id) {
        this.serverName = serverName;
        this.requestMethod = requestMethod;
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getPath() {
        return "/" + this.getServerName() + this.getId();
    }
}
