package org.hunter.skeleton.repository;

import org.hunter.pocket.session.Session;

/**
 * @author wujianchuan 2019/2/11
 */
public abstract class AbstractRepository implements Repository {

    public ThreadLocal<Session> sessionThreadLocal;

    @Override
    public void setSession(ThreadLocal<Session> sessionThreadLocal) {
        this.sessionThreadLocal = sessionThreadLocal;
    }
}
