package org.hv.biscuits.spine.utils;

import org.hv.biscuits.annotation.Affairs;
import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.Preferences;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * @author leyan95
 */
@Service(session = "biscuits")
public class PreferencesUtil extends AbstractService {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${biscuits.stationId:}")
    private String stationCode;
    private String serviceId;

    @PostConstruct
    public void init() {
        serviceId = applicationName.replaceAll("\\d+", "");
    }

    /**
     * 获取首选项
     *
     * @param bundleId 功能点编号
     * @param id       首选项编号
     * @return 首选项实例
     */
    @Affairs(on = false)
    public Preferences loadPreferences(String bundleId, String id) {
        Criteria criteria = this.getSession().createCriteria(Preferences.class)
                .add(Restrictions.equ("stationCode", stationCode))
                .add(Restrictions.equ("serviceId", serviceId))
                .add(Restrictions.equ("bundleId", bundleId))
                .add(Restrictions.equ("id", id));
        return criteria.unique();
    }

    /**
     * 获取首选项
     *
     * @param serviceId 服务编码
     * @param bundleId  功能点编号
     * @param id        首选项编号
     * @return 首选项实例
     */
    @Affairs(on = false)
    public Preferences loadPreferences(String serviceId, String bundleId, String id) {
        Criteria criteria = this.getSession().createCriteria(Preferences.class)
                .add(Restrictions.equ("stationCode", stationCode))
                .add(Restrictions.equ("serviceId", serviceId))
                .add(Restrictions.equ("bundleId", bundleId))
                .add(Restrictions.equ("id", id));
        return criteria.unique();
    }
}
