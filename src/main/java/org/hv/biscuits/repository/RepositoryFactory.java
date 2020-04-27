package org.hv.biscuits.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan
 */
public class RepositoryFactory {
    private final static Map<String, List<AbstractRepository>> FACTORY = new ConcurrentHashMap<>();

    public static void register(String serviceClazzName, AbstractRepository repository) {
        if (FACTORY.containsKey(serviceClazzName)) {
            List<AbstractRepository> repositories = FACTORY.get(serviceClazzName);
            if (!repositories.contains(repository)) {
                repositories.add(repository);
            }
        } else {
            List<AbstractRepository> repositories = new LinkedList<>();
            repositories.add(repository);
            FACTORY.put(serviceClazzName, repositories);
        }
    }

    public static List<AbstractRepository> getRepositories(String serviceClazzName) {
        return FACTORY.get(serviceClazzName);
    }
}
