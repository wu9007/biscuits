package org.hunter.skeleton.repository;

import org.hunter.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public abstract class AbstractRepository implements Repository {

    protected Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
}
