package org.hv.biscuits.log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author leyan95
 */
@Configuration
public class LogThreadPoolConfig {
    /**
     * 控制器日志线程池
     *
     * @return executor service
     */
    @Bean(value = "controllerLogThreadPool")
    public ExecutorService buildControllerLogThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("log-controller-thread-%d").build();
        return new ThreadPoolExecutor(5, 10, 50L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(50), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 消费者日志线程池
     *
     * @return executor service
     */
    @Bean(value = "consumerLogThreadPool")
    public ExecutorService buildConsumerLogThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("log-consumer-thread-%d").build();
        return new ThreadPoolExecutor(5, 10, 50L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(50), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
