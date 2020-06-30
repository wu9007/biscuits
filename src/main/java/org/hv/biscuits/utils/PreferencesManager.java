package org.hv.biscuits.utils;

import org.hv.biscuits.spine.model.Preferences;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public class PreferencesManager {
    private final Session session;
    private final String serviceId;

    private PreferencesManager(Session session, String serviceId) {
        this.session = session;
        this.serviceId = serviceId;
    }

    public static PreferencesManager newInstance(String sessionId, String serviceId) {
        Session session = SessionFactory.getSession(sessionId);
        return new PreferencesManager(session, serviceId);
    }

    String getPreferences(String stationCode, String bundleId, String preferencesId) throws SQLException {
        session.open();
        Criteria criteria = session.createCriteria(Preferences.class)
                .add(Restrictions.equ("stationCode", stationCode))
                .add(Restrictions.equ("serviceId", serviceId))
                .add(Restrictions.equ("bundleId", bundleId))
                .add(Restrictions.equ("id", preferencesId));
        Preferences preferences = criteria.unique();
        session.close();
        return preferences.getValue();
    }
}
