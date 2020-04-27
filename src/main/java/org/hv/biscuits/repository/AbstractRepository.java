package org.hv.biscuits.repository;

import org.hv.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public abstract class AbstractRepository {

    private final ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

    public void injectSession(Session session) {
        this.sessionLocal.set(session);
    }

    public void pourSession() {
        this.sessionLocal.remove();
    }

    public Session getSession() {
        return this.sessionLocal.get();
    }
}
