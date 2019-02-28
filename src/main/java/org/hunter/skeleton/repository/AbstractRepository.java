package org.hunter.skeleton.repository;

import org.hunter.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public abstract class AbstractRepository implements Repository {

    private ThreadLocal<Session> sessionLocal;

    @Override
    public void construct(ThreadLocal<Session> sessionLocal) {
        this.sessionLocal = sessionLocal;
    }

    protected Session getSession() {
        return this.sessionLocal.get();
    }
}
