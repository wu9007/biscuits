package org.hv.biscuits.service;

import org.hv.biscuits.repository.CommonRepository;
import org.hv.biscuits.repository.Repository;
import org.hv.pocket.session.Session;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujianchuan 2019/1/31
 */
public abstract class AbstractService implements Service {
    /**
     * 在切面中进行了赋值操作，而后将 session 注入到所以来的 repository 中。
     */
    private ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Field> repositoryFieldList = new ArrayList<>();

    public AbstractService() {
        for (Field field : this.getClass().getDeclaredFields()) {
            for (Type genericInterface : field.getType().getGenericInterfaces()) {
                if (genericInterface.equals(Repository.class) || genericInterface.getTypeName().contains(CommonRepository.class.getName())) {
                    this.repositoryFieldList.add(field);
                    break;
                }
            }
        }
    }

    @Override
    public Session getSession() {
        return this.sessionLocal.get();
    }

    /*protected void init() throws Exception {
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object target = field.get(this);
            if (target instanceof Repository) {
                this.repositoryList.add((Repository) target);
            }
        }
    }*/
}
