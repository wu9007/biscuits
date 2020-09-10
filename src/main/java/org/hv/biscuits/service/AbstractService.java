package org.hv.biscuits.service;

import org.hv.biscuits.core.session.ActiveSessionCenter;
import org.hv.pocket.session.Session;

/**
 * @author leyan95 2019/1/31
 */
public abstract class AbstractService implements Service {

    @Override
    public Session getSession() {
        return ActiveSessionCenter.getCurrentSession();
    }
}
