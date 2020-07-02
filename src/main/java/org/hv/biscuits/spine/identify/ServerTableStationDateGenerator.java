package org.hv.biscuits.spine.identify;

import org.hv.biscuits.spine.model.TableIdView;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.identify.AbstractIdentifyGenerator;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.MapperFactory;
import org.hv.pocket.query.SQLQuery;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wujianchuan 2019/1/2
 */
@Component
public class ServerTableStationDateGenerator extends AbstractIdentifyGenerator {

    @Value("${biscuits.serverId}")
    private int serverId;
    @Value("${biscuits.stationId}")
    private int stationId;

    @Override
    public void setGeneratorId() {
        this.generationType = BisGenerationType.SERVER_TABLE_STATION_DATE_INCREMENT;
    }

    @Override
    public Serializable getIdentify(Class<? extends AbstractEntity> clazz, Session session) throws SQLException {
        String tableName = MapperFactory.getTableName(clazz.getName());
        AtomicLong serialNumber = POOL.getOrDefault(tableName, new AtomicLong(0L));
        String localDateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String preStr = String.format("%03d", serverId) + this.getTableId(clazz) + String.format("%05d", stationId);
        long baseIdentify = Long.parseLong(localDateStr) * 1000000;
        if (serialNumber.get() == 0) {
            synchronized (this) {
                serialNumber = POOL.getOrDefault(tableName, new AtomicLong(0L));
                if (serialNumber.get() == 0) {
                    String maxIdentify = String.valueOf(this.getMaxIdentify(session, clazz));
                    if (maxIdentify != null) {
                        long maxSerialNumber = Long.parseLong(maxIdentify.replace(preStr, ""));
                        serialNumber.addAndGet(Math.max(maxSerialNumber, baseIdentify));
                    } else {
                        serialNumber = new AtomicLong(baseIdentify);
                    }
                }
                serialNumber.incrementAndGet();
                POOL.put(tableName, serialNumber);
                return preStr + serialNumber.get();
            }
        } else {
            if (serialNumber.get() < baseIdentify) {
                serialNumber = new AtomicLong(baseIdentify);
            }
            return preStr + serialNumber.incrementAndGet();
        }
    }

    private Serializable getMaxIdentify(Session session, Class<? extends AbstractEntity> clazz) {
        String identifyFieldName = MapperFactory.getIdentifyFieldName(clazz.getName());
        Criteria criteria = session.createCriteria(clazz);
        return (Serializable) criteria.max(identifyFieldName);
    }

    private String getTableId(Class<? extends AbstractEntity> clazz) throws SQLException {
        Session session = SessionFactory.getSession("biscuits");
        session.open();
        String tableName = MapperFactory.getTableName(clazz.getName());
        SQLQuery query = session.createSQLQuery("SELECT T.TABLE_NAME AS tableName, T.TABLE_ID AS tableId FROM T_TABLE_NAME_ID T WHERE T.TABLE_NAME = :TABLE_NAME", TableIdView.class)
                .setParameter("TABLE_NAME", tableName);
        TableIdView tableIdView = (TableIdView) query.unique();
        session.close();
        if (tableIdView == null) {
            throw new NullPointerException(String.format("数据库表T_TABLE_NAME_ID中未找到TABLE_NAME为%s的数据", tableName));
        }
        return tableIdView.getTableId();
    }
}
