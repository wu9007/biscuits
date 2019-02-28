package org.hunter.skeleton.repository;

import org.hunter.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public abstract class AbstractRepository implements Repository {

    private ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

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
