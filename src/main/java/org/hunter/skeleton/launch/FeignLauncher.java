package org.hunter.skeleton.launch;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.hunter.skeleton.annotation.FeignClient;
import org.hunter.skeleton.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujianchuan 2019/1/22
 */
@Component
public class FeignLauncher implements ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext
            applicationContext;

    private final
    ServiceConfig serviceConfig;

    private final ResourceLoader resourceLoader;

    @Autowired
    public FeignLauncher(ApplicationContext applicationContext, ServiceConfig serviceConfig, ResourceLoader resourceLoader) {
        this.applicationContext = applicationContext;
        this.serviceConfig = serviceConfig;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        Map<String, Class> serviceMap = this.getFeignMap();
        if (serviceMap != null && serviceMap.size() > 0) {
            serviceConfig.getNode().forEach(node -> {
                Class clazz = serviceMap.get(node.getName());
                Object bean = Feign.builder()
                        .encoder(new JacksonEncoder())
                        .decoder(new JacksonDecoder())
                        .options(new Request.Options(2000, 3500))
                        .retryer(new Retryer.Default(5000, 5000, 3))
                        .target(clazz, node.getUrl());
                applicationContext.getAutowireCapableBeanFactory().applyBeanPostProcessorsAfterInitialization(bean, bean.getClass().getName());
                Objects.requireNonNull(defaultListableBeanFactory).registerSingleton(bean.getClass().getName(), bean);
            });
        }
    }

    private Map<String, Class> getFeignMap() {
        Resource[] resources;
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        try {
            resources = resolver.getResources("classpath*:org/hunter/**/feign/**/*.class");
            MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
            return Arrays.stream(resources).map(resource -> {
                try {
                    MetadataReader reader = metaReader.getMetadataReader(resource);
                    Class clazz = Objects.requireNonNull(resourceLoader.getClassLoader()).loadClass(reader.getClassMetadata().getClassName());
                    FeignClient feignClient = (FeignClient) clazz.getAnnotation(FeignClient.class);
                    return new FeignInfo(feignClient, clazz);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull)
                    .filter(feignInfo -> feignInfo.getFeignClient() != null)
                    .collect(Collectors.toMap(info -> info.getFeignClient().name(), FeignInfo::getClazz));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class FeignInfo {
        private FeignClient feignClient;
        private Class clazz;

        FeignInfo(FeignClient feignClient, Class clazz) {
            this.feignClient = feignClient;
            this.clazz = clazz;
        }

        FeignClient getFeignClient() {
            return feignClient;
        }

        Class getClazz() {
            return clazz;
        }
    }
}
