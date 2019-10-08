package org.hv.biscuits.repository;

import org.hv.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public interface Repository {

    /**
     * 注入 session
     *
     * @param session session.
     */
    void injectSession(Session session);

    /**
     * 移除 session
     */
    void pourSession();

    /**
     * 获取 session
     *
     * @return session.
     */
    Session getSession();
}
