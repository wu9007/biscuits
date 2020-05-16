package org.hv.biscuits.log;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

/**
 * 日志队列
 *
 * @author wujianchuan
 */
@Component
public class LogQueue {
    /**
     * 控制器日志队列
     */
    private final Queue<AccessorLogView> ACCESSOR_LOG_QUEUE = new ConcurrentLinkedQueue<>();

    public void offerAccessorLog(AccessorLogView accessorLogView) {
        ACCESSOR_LOG_QUEUE.offer(accessorLogView);
    }

    public synchronized Queue<AccessorLogView> pollAccessorLog(int size) {
        Queue<AccessorLogView> queue = new ConcurrentLinkedQueue<>();
        int queueSize = ACCESSOR_LOG_QUEUE.size();
        if (queueSize < size) {
            size = queueSize;
        }
        IntStream.range(0, size).forEach(accessorLogView -> queue.add(ACCESSOR_LOG_QUEUE.poll()));
        return queue;
    }

    public AccessorLogView pollAccessorLog() {
        return ACCESSOR_LOG_QUEUE.poll();
    }
}
