package org.hv.biscuits.utils;

import org.hv.biscuits.spine.model.Station;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.hv.pocket.session.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 存放公共数据，如：血站信息
 *
 * @author wujianchuan 2020/8/5 09:59
 */
@Component
public class CommonInfoUtil {
    @Value("${biscuits.stationId:}")
    private String stationCode;
    private volatile Station station;

    public Station getStation() {
        if (station == null) {
            synchronized (this) {
                if (station == null) {
                    Session session = SessionFactory.getSession("biscuits");
                    session.open();
                    Transaction transaction;
                    try {
                        transaction = session.getTransaction();
                        transaction.begin();
                        this.initStationInfo(session);
                        transaction.commit();
                    } finally {
                        session.close();
                    }
                }
            }
        }
        return station;
    }

    private void initStationInfo(Session session) {
        Criteria criteria = session.createCriteria(Station.class)
                .add(Restrictions.equ("code", stationCode));
        station = criteria.unique();
    }
}
