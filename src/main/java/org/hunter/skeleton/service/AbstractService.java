package org.hunter.skeleton.service;

import org.hunter.pocket.session.Session;
import org.hunter.skeleton.repository.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wujianchuan 2019/1/31
 */
public class AbstractService {

    private ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

    private List<Repository> repositoryList = new ArrayList<>();

    protected void injectRepository(Repository... repositories) {
        this.repositoryList.addAll(Arrays.asList(repositories));
    }
}
