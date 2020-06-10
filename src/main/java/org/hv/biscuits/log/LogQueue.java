package org.hv.biscuits.log;

import org.hv.biscuits.log.model.AccessorLogView;
import org.hv.biscuits.log.model.OrmLogView;
import org.hv.biscuits.log.model.PersistenceLogView;
import org.hv.biscuits.log.model.ServiceLogView;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

/**
 * 日志队列（包含：控制器日志，消费者日志，业务执行日志，持久化执行日志）
 *
 * @author leyan95
 */
@Component
public class LogQueue {
    /**
     * 控制器日志队列
     */
    private final Queue<AccessorLogView> ACCESSOR_LOG_QUEUE = new ConcurrentLinkedQueue<>();
    /**
     * 工作单元日志队列
     */
    private final Queue<ServiceLogView> SERVICE_LOG_QUEUE = new ConcurrentLinkedQueue<>();
    /**
     * 仓储日志队列
     */
    private final Queue<PersistenceLogView> PERSISTENCE_LOG_QUEUE = new ConcurrentLinkedQueue<>();
    /**
     * Orm日志队列
     */
    private final Queue<OrmLogView> ORM_LOG_QUEUE = new ConcurrentLinkedQueue<>();

    /**
     * 提供控制器日志
     *
     * @param accessorLogView {@link AccessorLogView}
     */
    public void offerAccessorLog(AccessorLogView accessorLogView) {
        ACCESSOR_LOG_QUEUE.offer(accessorLogView);
    }

    /**
     * 提供工作单元业务执行日志
     *
     * @param serviceLogView {@link ServiceLogView}
     */
    public void offerServiceLog(ServiceLogView serviceLogView) {
        SERVICE_LOG_QUEUE.offer(serviceLogView);
    }

    /**
     * 提供仓储执行日志
     *
     * @param persistenceLogView {@link PersistenceLogView}
     */
    public void offerPersistenceLog(PersistenceLogView persistenceLogView) {
        PERSISTENCE_LOG_QUEUE.offer(persistenceLogView);
    }

    /**
     * 提供ORM执行日志
     *
     * @param ormLogView {@link OrmLogView}
     */
    public void offerOrmLog(OrmLogView ormLogView) {
        ORM_LOG_QUEUE.offer(ormLogView);
    }

    /**
     * 控制器日志批量出队，
     * 如果批量出队的日志数量大于队列数量则全部出队，
     * 否则出队指定数量的日志。
     *
     * @param size 日志数量
     * @return 日志实例集合
     */
    public synchronized Queue<AccessorLogView> pollAccessorLog(int size) {
        Queue<AccessorLogView> queue = new ConcurrentLinkedQueue<>();
        int queueSize = ACCESSOR_LOG_QUEUE.size();
        if (queueSize < size) {
            size = queueSize;
        }
        IntStream.range(0, size).forEach(index -> queue.add(this.pollAccessorLog()));
        return queue;
    }

    /**
     * 控制器日志头节点出队，
     * 如果队列是空队列则返回 {@code null}
     *
     * @return 日志实例
     */
    public AccessorLogView pollAccessorLog() {
        return ACCESSOR_LOG_QUEUE.poll();
    }

    /**
     * 工作单元日志批量出队，
     * 如果批量出队的日志数量大于队列数量则全部出队，
     * 否则出队指定数量的日志。
     *
     * @param size 日志数量
     * @return 日志实例集合
     */
    public synchronized Queue<ServiceLogView> pollServiceLog(int size) {
        Queue<ServiceLogView> queue = new ConcurrentLinkedQueue<>();
        int queueSize = SERVICE_LOG_QUEUE.size();
        if (queueSize < size) {
            size = queueSize;
        }
        IntStream.range(0, size).forEach(index -> queue.add(this.pollServiceLog()));
        return queue;
    }

    /**
     * 工作单元日志头节点出队，
     * 如果队列是空队列则返回 {@code null}
     *
     * @return 日志实例
     */
    public ServiceLogView pollServiceLog() {
        return SERVICE_LOG_QUEUE.poll();
    }

    /**
     * 仓储日志批量出队，
     * 如果批量出队的日志数量大于队列数量则全部出队，
     * 否则出队指定数量的日志。
     *
     * @param size 日志数量
     * @return 日志实例集合
     */
    public synchronized Queue<PersistenceLogView> pollPersistenceLog(int size) {
        Queue<PersistenceLogView> queue = new ConcurrentLinkedQueue<>();
        int queueSize = PERSISTENCE_LOG_QUEUE.size();
        if (queueSize < size) {
            size = queueSize;
        }
        IntStream.range(0, size).forEach(index -> queue.add(this.pollPersistenceLog()));
        return queue;
    }

    /**
     * 仓储日志头节点出队，
     * 如果队列是空队列则返回 {@code null}
     *
     * @return 日志实例
     */
    public PersistenceLogView pollPersistenceLog() {
        return PERSISTENCE_LOG_QUEUE.poll();
    }

    /**
     * ORM日志批量出队，
     * 如果批量出队的日志数量大于队列数量则全部出队，
     * 否则出队指定数量的日志。
     *
     * @param size 日志数量
     * @return 日志实例集合
     */
    public synchronized Queue<OrmLogView> pollOrmLog(int size) {
        Queue<OrmLogView> queue = new ConcurrentLinkedQueue<>();
        int queueSize = ORM_LOG_QUEUE.size();
        if (queueSize < size) {
            size = queueSize;
        }
        IntStream.range(0, size).forEach(index -> queue.add(this.pollOrmLog()));
        return queue;
    }

    /**
     * ORM日志头节点出队，
     * 如果队列是空队列则返回 {@code null}
     *
     * @return 日志实例
     */
    public OrmLogView pollOrmLog() {
        return ORM_LOG_QUEUE.poll();
    }
}
