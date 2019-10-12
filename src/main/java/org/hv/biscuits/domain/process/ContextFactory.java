package org.hv.biscuits.domain.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan
 */
public class ContextFactory {
    private static final ContextFactory INSTANCE = new ContextFactory();
    private final Map<String, Map<String, Node>> nodeMapperPool = new ConcurrentHashMap<>(16);
    private final Map<String, Context> contextPool = new ConcurrentHashMap<>(64);

    private ContextFactory() {
    }

    public static ContextFactory getInstance() {
        return INSTANCE;
    }

    void putNodeMapper(String name, Map<String, Node> nodeMapper) {
        this.nodeMapperPool.put(name, nodeMapper);
    }

    public Context buildProcessContext(String processName, String dataUuid, String[] sortedNodeNames) throws Exception {
        String contextPoolKey = processName + "_" + dataUuid;
        if (this.contextPool.containsKey(contextPoolKey)) {
            throw new Exception("This process already exists. Please don't create it again.");
        }
        Map<String, Node> nodeMapper = this.nodeMapperPool.get(processName);
        Context context = new ProcessContext(nodeMapper);
        context.setSortedNodeNames(sortedNodeNames);
        context.setDataUuid(dataUuid);
        this.contextPool.putIfAbsent(contextPoolKey, context);
        return context;
    }

    public Context getProcessContext(String processName, String dataUuid) {
        return this.contextPool.get(processName + "_" + dataUuid);
    }
}
