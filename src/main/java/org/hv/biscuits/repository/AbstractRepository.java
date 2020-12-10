package org.hv.biscuits.repository;

import org.hv.biscuits.core.session.ActiveSessionCenter;
import org.hv.pocket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leyan95 2019/2/11
 */
public abstract class AbstractRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepository.class);

    public Session getSession() {
        Session session = ActiveSessionCenter.getCurrentSession();
        if (session == null) {
            LOGGER.error("{} 中存在方法未开启数据库会话，请使用方法注解 @Affairs 进行开启。", this.getClass().getName());
        }
        return session;
    }
}
