package org.hv.biscuit.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan
 */
public class RepositoryFactory {
    private final static Map<String, List<Repository>> FACTORY = new ConcurrentHashMap<>();

    public static void register(String serviceClazzName, Repository repository) {
        if (FACTORY.containsKey(serviceClazzName)) {
            List<Repository> repositories = FACTORY.get(serviceClazzName);
            if (!repositories.contains(repository)) {
                repositories.add(repository);
            }
        } else {
            List<Repository> repositories = new LinkedList<>();
            repositories.add(repository);
            FACTORY.put(serviceClazzName, repositories);
        }
    }

    public static List<Repository> getRepositories(String serviceClazzName) {
        return FACTORY.get(serviceClazzName);
    }
}
