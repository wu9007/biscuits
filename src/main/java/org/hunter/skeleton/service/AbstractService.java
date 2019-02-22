package org.hunter.skeleton.service;

import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionImpl;

/**
 * @author wujianchuan 2019/1/31
 */
public class AbstractService {

    protected Session session = new SessionImpl();
}
