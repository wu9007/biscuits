package org.hunter.skeleton.service;

import org.hunter.pocket.session.Session;
import org.hunter.skeleton.repository.Repository;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujianchuan 2019/1/31
 */
public class AbstractService {
    /**
     * 在切面中进行了赋值操作，而后将 session 注入到所以来的 repository 中。
     */
    private ThreadLocal<Session> sessionLocal = new ThreadLocal<>();

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Field> repositoryFieldList = new ArrayList<>();

    public AbstractService() {
        for (Field field : this.getClass().getDeclaredFields()) {
            for (Type genericInterface : field.getType().getGenericInterfaces()) {
                if (genericInterface.equals(Repository.class) || Repository.class.isAssignableFrom(((ParameterizedTypeImpl) genericInterface).getRawType())) {
                    this.repositoryFieldList.add(field);
                    break;
                }
            }
        }
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
