package org.hunter.skeleton.service;

import org.hunter.pocket.session.Session;

/**
 * @author wujianchuan 2019/1/31
 */
public class AbstractService {

        protected ThreadLocal<Session> sessionLocal = new ThreadLocal<>();
}
