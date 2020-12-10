package org.hv.biscuits.spine.identify;

import org.hv.biscuits.spine.model.TableNameId;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.identify.AbstractIdentifyGenerator;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.MapperFactory;
import org.hv.pocket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

/**
 * @author wujianchuan 2019/1/2
 */
@Component
public class ServerTableStationDateGenerator extends AbstractIdentifyGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTableStationDateGenerator.class);
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${biscuits.serverId}")
    private int serverId;
    @Value("${biscuits.stationId}")
    private int stationId;
    private static final UnaryOperator<BigInteger> INTEGER_UNARY_OPERATOR = (item) -> item.add(BigInteger.ONE);
    /**
     * 类名与类对应的数据库表编号
     */
    private static final Map<String, String> CLASS_ID_MAPPER = new HashMap<>(200);

    @Override
    public void setGeneratorId() {
        this.generationType = BisGenerationType.SERVER_TABLE_STATION_DATE_INCREMENT;
    }

    @Override
    public Serializable getIdentify(Class<? extends AbstractEntity> clazz, Session session) {
        String tableName = MapperFactory.getTableName(clazz.getName());
        AtomicReference<BigInteger> serialNumber = POOL.getOrDefault(tableName, new AtomicReference<>(BigInteger.ZERO));
        String preStr = String.format("%04d", serverId) + this.getTableId(clazz, session) + String.format("%05d", stationId) + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long baseIdentify = Long.parseLong("0");
        if (serialNumber.get().compareTo(BigInteger.ZERO) == 0) {
            synchronized (this) {
                serialNumber = POOL.getOrDefault(tableName, new AtomicReference<>(BigInteger.ZERO));
                if (serialNumber.get().compareTo(BigInteger.ZERO) == 0) {
                    Serializable maxIdentify = this.getTodayMaxIdentify(session, clazz, preStr);
                    if (maxIdentify != null) {
                        long maxSerialNumber = Long.parseLong(String.valueOf(maxIdentify).replace(preStr, ""));
                        serialNumber.set(new BigInteger(String.valueOf(Math.max(maxSerialNumber, baseIdentify))));
                    } else {
                        serialNumber = new AtomicReference<>(new BigInteger(String.valueOf(baseIdentify)));
                    }
                    serialNumber.updateAndGet(INTEGER_UNARY_OPERATOR);
                    POOL.put(tableName, serialNumber);
                    return preStr + String.format("%06d", serialNumber.get());
                } else {
                    return preStr + String.format("%06d", serialNumber.updateAndGet(INTEGER_UNARY_OPERATOR));
                }
            }
        } else {
            return preStr + String.format("%06d", serialNumber.updateAndGet(INTEGER_UNARY_OPERATOR));
        }
    }

    private Serializable getTodayMaxIdentify(Session session, Class<? extends AbstractEntity> clazz, String preStr) {
        String identifyFieldName = MapperFactory.getIdentifyFieldName(clazz.getName());
        Criteria criteria = session.createCriteria(clazz).specifyField("uuid")
                .add(Restrictions.like(identifyFieldName, preStr + "%"));
        List<AbstractEntity> list = criteria.list();
        int maxNumber = list.stream().map(item -> Integer.parseInt(((String) item.loadIdentify()).replace(preStr, ""))).max(Comparator.naturalOrder()).orElse(0);
        LOGGER.debug("获取的 {} 中最大序号为 >>> {}", clazz.getSimpleName(), maxNumber);
        return maxNumber;
    }

    private String getTableId(Class<? extends AbstractEntity> clazz, Session session) {
        String tableId = CLASS_ID_MAPPER.get(clazz.getName());
        if (tableId != null) {
            return tableId;
        } else {
            synchronized (this) {
                String serviceId = applicationName.replaceAll("\\d+", "").toUpperCase();
                tableId = CLASS_ID_MAPPER.get(clazz.getName());
                if (tableId == null) {
                    String tableName = MapperFactory.getTableName(clazz.getName());
                    Criteria criteria = session.createCriteria(TableNameId.class)
                            .add(Restrictions.equ("tableName", tableName))
                            .add(Restrictions.or(Restrictions.equ("serviceId", serviceId), Restrictions.isNull("serviceId")));
                    TableNameId tableNameId = criteria.unique();
                    if (tableNameId == null) {
                        throw new NullPointerException(String.format("数据库表T_TABLE_NAME_ID中未找到TABLE_NAME为%s的数据", tableName));
                    }
                    CLASS_ID_MAPPER.put(clazz.getName(), tableNameId.getTableId());
                }
                return CLASS_ID_MAPPER.get(clazz.getName());
            }
        }
    }
}
