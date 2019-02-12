package org.hunter.skeleton.launcher;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionFactory;
import org.hunter.pocket.session.Transaction;
import org.hunter.skeleton.annotation.Auth;
import org.hunter.skeleton.controller.AbstractController;
import org.hunter.skeleton.model.AuthMapperRelation;
import org.hunter.skeleton.model.Authority;
import org.hunter.skeleton.model.Mapper;
import org.hunter.skeleton.permission.Permission;
import org.hunter.skeleton.permission.PermissionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/2/2
 */
@Component
@Order(1)
public class AuthLauncher implements CommandLineRunner {

    private final
    Map<String, AbstractController> controllerMap;

    private final
    List<Permission> permissionList;

    private final
    ApplicationContext context;

    private String serverName;

    @Autowired
    public AuthLauncher(Map<String, AbstractController> controllerMap, List<Permission> permissionList, ApplicationContext context) {
        this.controllerMap = controllerMap;
        this.context = context;
        this.permissionList = permissionList;
    }

    @Override
    public void run(String... args) throws Exception {
        this.serverName = Objects.requireNonNull(this.context.getEnvironment().getProperty("spring.application.name")).toUpperCase();

        Session session = SessionFactory.getSession("skeleton");
        session.open();
        Transaction transactional = session.getTransaction();
        transactional.begin();
        PermissionFactory.init(this.initPermissionMap(session));
        this.permissionList.forEach(Permission::init);
        Map<String, Mapper> oldMapperMap = this.getMapperMap(session);
        this.deleteUselessMapper(session, oldMapperMap);
        Map<String, AuthMapperRelation> oldAuthMapperIdMap = this.getAuthMapperIdMap(session);
        this.deleteUselessAuthMapper(session, oldAuthMapperIdMap);
        saveAuthAndMapper(session, oldMapperMap, oldAuthMapperIdMap);
        transactional.commit();
        session.close();
    }

    private Map<String, Authority> initPermissionMap(Session session) throws Exception {
        Criteria criteria = session.creatCriteria(Authority.class);
        criteria.add(Restrictions.equ("serverName", serverName));
        List<Authority> authorityList = criteria.list();
        return authorityList.stream().collect(Collectors.toMap(item -> item.getServerName() + "_" + item.getId(), item -> item));
    }

    private Map<String, Mapper> getMapperMap(Session session) throws Exception {
        Criteria mapperCriteria = session.creatCriteria(Mapper.class);
        mapperCriteria.add(Restrictions.equ("serverName", serverName));
        List<Mapper> mapperList = mapperCriteria.list();
        return mapperList.stream().collect(Collectors.toMap(Mapper::getId, item -> item));
    }

    private Map<String, AuthMapperRelation> getAuthMapperIdMap(Session session) throws Exception {
        Criteria criteria = session.creatCriteria(AuthMapperRelation.class);
        criteria.add(Restrictions.equ("serverName", serverName));
        List<AuthMapperRelation> authMapperList = criteria.list();
        return authMapperList.stream()
                .collect(Collectors.toMap(item -> {
                    try {
                        Authority authority = (Authority) session.findOne(Authority.class, item.getAuthUuid());
                        Mapper mapper = (Mapper) session.findOne(Mapper.class, item.getMapperUuid());
                        return authority.getServerName() + "_" + authority.getId() + "_" + mapper.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }, item -> item));
    }

    private void saveAuthAndMapper(Session session, Map<String, Mapper> oldMapperMap, Map<String, AuthMapperRelation> oldAuthMapperIdMap) {
        controllerMap.forEach((k, v) -> {
            Arrays.stream(v.getClass().getMethods())
                    .filter(method -> method.getAnnotation(Auth.class) != null)
                    .forEach(method -> {
                        Mapper mapper = this.getMethodMapper(method);
                        try {
                            Auth auth = method.getAnnotation(Auth.class);
                            String auhId = auth.value();
                            Authority authority = PermissionFactory.get(serverName, auhId);
                            if (authority != null) {
                                if (!oldMapperMap.containsKey(mapper.getId())) {
                                    session.save(mapper);
                                } else {
                                    mapper = oldMapperMap.get(mapper.getId());
                                }
                                AuthMapperRelation authMapper;
                                if (!oldAuthMapperIdMap.containsKey(authority.getServerName() + "_" + authority.getId().toString() + "_" + mapper.getId())) {
                                    authMapper = new AuthMapperRelation(serverName, authority.getUuid(), mapper.getUuid());
                                    session.save(authMapper);
                                }
                            } else {
                                throw new RuntimeException("ServerName = " + serverName + " MapperId = " + mapper.getId() + "   未找到对应权限");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    /**
     * 删除数据库多余的Mapper
     *
     * @param session      session
     * @param oldMapperMap 数据库中查询所得的Mapper
     */
    private void deleteUselessMapper(Session session, Map<String, Mapper> oldMapperMap) {
        Map<String, Mapper> modernMapperMap = controllerMap.entrySet().stream()
                .map(controller ->
                        Arrays.stream(controller.getValue().getClass().getMethods())
                                .filter(this.isMapperMethod)
                                .map(this::getMethodMapper)
                                .collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Mapper::getId, item -> item));
        oldMapperMap.entrySet().stream()
                .filter((item -> !modernMapperMap.containsKey(item.getKey())))
                .forEach(item -> {
                    try {
                        Mapper mapper = item.getValue();
                        Criteria criteria = session.creatCriteria(AuthMapperRelation.class);
                        criteria.add(Restrictions.equ("mapperUuid", mapper.getUuid()));
                        criteria.delete();
                        session.delete(mapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private Predicate<Method> isMapperMethod = method -> method.getAnnotation(RequestMapping.class) != null || method.getAnnotation(GetMapping.class) != null || method.getAnnotation(PostMapping.class) != null;

    /**
     * 删除数据库多余的 AuthMapperRelation
     *
     * @param session                  session
     * @param oldAuthMapperRelationMap 数据库查询出的 AuthMapperRelation
     */
    private void deleteUselessAuthMapper(Session session, Map<String, AuthMapperRelation> oldAuthMapperRelationMap) {
        String modernAuthMapperRelationMapKeys = controllerMap.entrySet().stream()
                .map(controller ->
                        Arrays.stream(controller.getValue().getClass().getMethods())
                                .filter(this.isMapperMethod)
                                .map(method -> {
                                    Mapper mapper = this.getMethodMapper(method);
                                    Auth auth = method.getAnnotation(Auth.class);
                                    return this.serverName + "_" + auth.value() + "_" + mapper.getId();
                                })
                                .collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
                .collect(Collectors.joining(" ,"));
        oldAuthMapperRelationMap.keySet().stream()
                .filter(item -> !modernAuthMapperRelationMapKeys.contains(item))
                .forEach(item -> {
                    try {
                        session.delete(oldAuthMapperRelationMap.get(item));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    private Mapper getMethodMapper(Method method) {
        String mapperId;
        String requestMethod;
        if (method.getAnnotation(RequestMapping.class) != null) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            mapperId = String.join(",", Arrays.asList(requestMapping.value()));
            requestMethod = requestMapping.method().length > 0
                    ? Arrays.stream(requestMapping.method())
                    .map(item -> {
                        if (RequestMethod.GET.equals(item)) {
                            return "get";
                        } else if (RequestMethod.POST.equals(item)) {
                            return "post";
                        } else {
                            throw new RuntimeException("不支持" + item.name() + "请求方式");
                        }
                    })
                    .collect(Collectors.joining(","))
                    : "get,post";

        } else if (method.getAnnotation(GetMapping.class) != null) {
            mapperId = String.join(",", Arrays.asList(method.getAnnotation(GetMapping.class).value()));
            requestMethod = "get";
        } else if (method.getAnnotation(PostMapping.class) != null) {
            mapperId = String.join(",", Arrays.asList(method.getAnnotation(PostMapping.class).value()));
            requestMethod = "post";
        } else {
            throw new RuntimeException("未找到注解");
        }
        return new Mapper(this.serverName, requestMethod, mapperId);
    }
}
