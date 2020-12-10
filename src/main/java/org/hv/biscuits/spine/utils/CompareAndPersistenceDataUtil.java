package org.hv.biscuits.spine.utils;

import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.hv.pocket.session.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujianc
 */
public class CompareAndPersistenceDataUtil {
    private final Logger logger = LoggerFactory.getLogger(CompareAndPersistenceDataUtil.class);
    private final String sessionName;

    private CompareAndPersistenceDataUtil(String sessionName) {
        this.sessionName = sessionName;
    }

    public static CompareAndPersistenceDataUtil newInstance(String sessionName) {
        return new CompareAndPersistenceDataUtil(sessionName);
    }

    public <T extends AbstractEntity> void compareAndPersistenceData(List<T> sourceData, Class<T> clazz, String businessName) {
        Session session = SessionFactory.getSession(sessionName);
        session.open();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
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
                    session.delete(data, false);
                }
                logger.info("========================== 同步数据过程中删除 {} 条{}数据 ==========================", deleteData.size(), businessName);
            }
            if (saveData.size() > 0) {
                for (T data : saveData) {
                    session.save(data, false);
                }
                logger.info("========================== 同步数据过程中新增 {} 条{}数据  ==========================", saveData.size(), businessName);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollBack();
            logger.warn("{} 数据同步失败, 异常信息：{}", businessName, e.getMessage());
        } finally {
            session.close();
        }

    }
}
