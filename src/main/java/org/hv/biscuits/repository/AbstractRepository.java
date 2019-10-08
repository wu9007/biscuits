package org.hv.biscuits.repository;

import org.hv.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public abstract class AbstractRepository implements Repository {

    private final ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

    @Override
    public void injectSession(Session session) {
        this.sessionLocal.set(session);
    }

    @Override
    public void pourSession() {
        this.sessionLocal.remove();
    }

    @Override
    public Session getSession() {
        return this.sessionLocal.get();
    }
}
