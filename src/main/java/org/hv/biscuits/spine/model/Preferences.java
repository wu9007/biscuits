package org.hv.biscuits.spine.model;

import org.hv.pocket.annotation.Column;
import org.hv.pocket.annotation.Entity;
import org.hv.pocket.model.BaseEntity;

/**
 * @author wujianchuan
 */
@Entity(table = "T_PREFERENCES", tableId = 120, businessName = "首选项")
public class Preferences extends BaseEntity {
    private static final long serialVersionUID = -1944222721422088148L;

    @Column
    private String id;
    @Column
    private String description;
    @Column
    private String value;
    @Column
    private String name;
    @Column
    private String stationCode;
    @Column
    private String serviceId;
    @Column
    private String bundleId;
    @Column
    private String regex;
    @Column
    private String alert;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }
}
