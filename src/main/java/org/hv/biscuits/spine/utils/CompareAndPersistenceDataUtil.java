package org.hv.biscuits.spine.utils;

import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujianc
 */
public class CompareAndPersistenceDataUtil {
    private final Logger logger = LoggerFactory.getLogger(CompareAndPersistenceDataUtil.class);
    private static final CompareAndPersistenceDataUtil INSTANCE = new CompareAndPersistenceDataUtil();

    private CompareAndPersistenceDataUtil() {
    }

    public static CompareAndPersistenceDataUtil getInstance() {
        return INSTANCE;
    }

    public <T extends AbstractEntity> void compareAndPersistenceData(List<T> sourceData, Session session, Class<T> clazz, String businessName) throws SQLException, IllegalAccessException {
        List<T> ownData = session.list(clazz, false);
        List<T> deleteData = new ArrayList<>();
        List<T> saveData = new ArrayList<>();
        ownData.forEach(data -> {
            if (!sourceData.contains(data)) {
                deleteData.add(data);
            }
        });
        sourceData.forEach(data -> {
            if (!ownData.contains(data)) {
                saveData.add(data);
            }
        });
        if (deleteData.size() > 0) {
            for (T data : deleteData) {
                session.delete(data);
            }
            logger.info("========================== 同步数据过程中删除 {} 条{}数据 ==========================", deleteData.size(), businessName);
        }
        if (saveData.size() > 0) {
            for (T data : saveData) {
                session.save(data, false);
            }
            logger.info("========================== 同步数据过程中新增 {} 条{}数据  ==========================", saveData.size(), businessName);
        }
    }
}
