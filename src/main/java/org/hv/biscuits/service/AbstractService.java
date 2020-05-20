package org.hv.biscuits.service;

import org.hv.pocket.session.Session;

/**
 * @author leyan95 2019/1/31
 */
public abstract class AbstractService implements Service {
    /**
     * 在切面中进行了赋值操作，而后将 session 注入到所以来的 repository 中。
     */
    private ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

    @Override
    public Session getSession() {
        return this.sessionLocal.get();
    }
}
