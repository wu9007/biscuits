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
    /**
     * 在切面中进行了赋值操作，而后将 session 注入到所以来的 repository 中。
     */
    private ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

    private final List<Repository> repositoryList = new ArrayList<>();

    protected void injectRepository(Repository... repositories) {
        this.repositoryList.addAll(Arrays.asList(repositories));
    }
}
