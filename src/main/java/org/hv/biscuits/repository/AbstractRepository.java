package org.hv.biscuits.repository;

import org.hv.biscuits.core.session.ActiveSessionCenter;
import org.hv.pocket.session.Session;

/**
 * @author leyan95 2019/2/11
 */
public abstract class AbstractRepository {

    public Session getSession() {
        return ActiveSessionCenter.getCurrentSession();
    }
}
