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
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        Map<String, AuthMapperRelation> authMapperIdMap = this.getAuthMapperIdMap(session);
        saveAuthAndMapper(session, oldMapperMap, authMapperIdMap);
        transactional.commit();
        session.close();
    }

    private Map<String, Authority> initPermissionMap(Session session) throws Exception {
        Criteria criteria = session.creatCriteria(Authority.class);
        criteria.add(Restrictions.equ("serverName", serverName));
        List<Authority> authorityList = criteria.list();
        return authorityList.stream().collect(Collectors.toMap(item -> item.getServerName() + item.getId(), item -> item));
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
                .collect(Collectors.toMap(item -> item.getAuthUuid().toString() + item.getMapperUuid(), item -> item));
    }

    private void saveAuthAndMapper(Session session, Map<String, Mapper> mapperMap, Map<String, AuthMapperRelation> authMapperIdMap) {
        controllerMap.forEach((k, v) -> {
            Arrays.stream(v.getClass().getMethods())
                    .filter(method -> method.getAnnotation(Auth.class) != null)
                    .forEach(method -> {
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

                        try {
                            Auth auth = method.getAnnotation(Auth.class);
                            String auhId = auth.value();
                            Authority authority = PermissionFactory.get(serverName, auhId);
                            if (authority != null) {
                                Mapper mapper;
                                if (!mapperMap.containsKey(mapperId)) {
                                    mapper = new Mapper(serverName, requestMethod, mapperId);
                                    session.save(mapper);
                                } else {
                                    mapper = mapperMap.get(mapperId);
                                }
                                AuthMapperRelation authMapper;
                                if (!authMapperIdMap.containsKey(authority.getUuid().toString() + mapper.getUuid())) {
                                    authMapper = new AuthMapperRelation(serverName, authority.getUuid(), mapper.getUuid());
                                    session.save(authMapper);
                                }
                            } else {
                                throw new RuntimeException(serverName + mapperId + "未找到对应权限");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    private void deleteMapper(Session session, Map<String, Mapper> oldMapperMap) {
        //TODO 删除Mapper
    }
    //TODO 删除AuthMapperRelation
}
