package org.hv.biscuits.spine.identify;

import org.hv.biscuits.spine.model.TableNameId;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.identify.AbstractIdentifyGenerator;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.MapperFactory;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
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
        String preStr = String.format("%04d", serverId) + this.getTableId(clazz) + String.format("%05d", stationId) + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long baseIdentify = Long.parseLong("0");
        if (serialNumber.get() == 0) {
            synchronized (this) {
                serialNumber = POOL.getOrDefault(tableName, new AtomicLong(0L));
                if (serialNumber.get() == 0) {
                    Serializable maxIdentify = this.getTodayMaxIdentify(session, clazz, preStr);
                    if (maxIdentify != null) {
                        long maxSerialNumber = Long.parseLong(String.valueOf(maxIdentify).replace(preStr, ""));
                        serialNumber.addAndGet(Math.max(maxSerialNumber, baseIdentify));
                    } else {
                        serialNumber = new AtomicLong(baseIdentify);
                    }
                }
                serialNumber.incrementAndGet();
                POOL.put(tableName, serialNumber);
                return preStr + String.format("%06d", serialNumber.get());
            }
        } else {
            return preStr + String.format("%06d", serialNumber.incrementAndGet());
        }
    }

    private Serializable getTodayMaxIdentify(Session session, Class<? extends AbstractEntity> clazz, String preStr) throws SQLException {
        String identifyFieldName = MapperFactory.getIdentifyFieldName(clazz.getName());
        Criteria criteria = session.createCriteria(clazz)
                .add(Restrictions.like(identifyFieldName, preStr + "%"));
        List<AbstractEntity> list = criteria.list();
        return list.stream().map(item -> Integer.parseInt(((String) item.loadIdentify()).replace(preStr, ""))).max(Comparator.naturalOrder()).orElse(0);
    }

    private String getTableId(Class<? extends AbstractEntity> clazz) throws SQLException {
        Session session = SessionFactory.getSession("biscuits");
        session.open();
        String tableName = MapperFactory.getTableName(clazz.getName());
        Criteria criteria = session.createCriteria(TableNameId.class);
        criteria.add(Restrictions.equ("tableName", tableName));
        TableNameId tableNameId = criteria.unique();
        session.close();
        if (tableNameId == null) {
            throw new NullPointerException(String.format("数据库表T_TABLE_NAME_ID中未找到TABLE_NAME为%s的数据", tableName));
        }
        return tableNameId.getTableId();
    }
}
