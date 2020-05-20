package org.hv.biscuits.log;

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
     * 提供控制器日志
     *
     * @param accessorLogView {@link AccessorLogView}
     */
    public void offerAccessorLog(AccessorLogView accessorLogView) {
        ACCESSOR_LOG_QUEUE.offer(accessorLogView);
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
        IntStream.range(0, size).forEach(accessorLogView -> queue.add(this.pollAccessorLog()));
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
}
