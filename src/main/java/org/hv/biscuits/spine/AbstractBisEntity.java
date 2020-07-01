package org.hv.biscuits.spine;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hv.biscuits.spine.identify.BisGenerationType;
import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Identify;
import org.hv.pocket.model.AbstractEntity;

import java.io.Serializable;

/**
 * @author wujianchuan 2018/12/26
 * 臣虽至愚至陋，何能有知，徒以忠愤所激，不能自已。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractBisEntity extends AbstractEntity {
    private static final long serialVersionUID = -7938933071639115214L;
    @Identify(strategy = BisGenerationType.SERVER_TABLE_STATION_DATE_INCREMENT)
    @Column(name = "UUID")
    private String uuid;

    @Override
    public Serializable loadIdentify() {
        return this.getUuid();
    }

    @Override
    public void putIdentify(Serializable identify) {
        this.setUuid((String) identify);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
