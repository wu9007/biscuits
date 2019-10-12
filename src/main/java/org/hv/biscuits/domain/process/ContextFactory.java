package org.hv.biscuits.domain.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan
 */
public class ContextFactory {
    private static final ContextFactory INSTANCE = new ContextFactory();
    private final Map<String, Context> contextMap = new ConcurrentHashMap<>(16);

    private ContextFactory() {
    }

    public static ContextFactory getInstance() {
        return INSTANCE;
    }

    void putContext(String name, Context context) {
        this.contextMap.put(name, context);
    }

    public Context getContext(String name, String[] sortedNodeNames) throws Exception {
        Context context = this.contextMap.get(name);
        context.setSortedNodeNames(sortedNodeNames);
        return context;
    }
}
