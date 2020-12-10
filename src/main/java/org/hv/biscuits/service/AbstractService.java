package org.hv.biscuits.service;

import org.hv.biscuits.core.session.ActiveSessionCenter;
import org.hv.pocket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leyan95 2019/1/31
 */
public abstract class AbstractService implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    @Override
    public Session getSession() {
        Session session = ActiveSessionCenter.getCurrentSession();
        if (session == null) {
            LOGGER.error("{} 中存在方法未开启数据库会话，请使用方法注解 @Affairs 进行开启。", this.getClass().getName());
        }
        return session;
    }
}
