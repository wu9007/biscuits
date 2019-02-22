package org.hunter.skeleton.repository;

import org.hunter.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public interface Repository {

    /**
     * 注入session
     *
     * @param session session
     */
    void setSession(Session session);
}
