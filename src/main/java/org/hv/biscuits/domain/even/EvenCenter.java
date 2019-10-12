package org.hv.biscuits.domain.even;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujianchuan
 */
public class EvenCenter {
    private final static EvenCenter INSTANCE = new EvenCenter();
    private final Map<String, Collection<Monitor>> monitorsMap = new ConcurrentHashMap<>(16);

    private EvenCenter() {
    }

    public static EvenCenter getInstance() {
        return INSTANCE;
    }

    public void fireEven(String evenSourceId, Object... args) throws Exception {
        Collection<Monitor> monitors = monitorsMap.get(evenSourceId);
        for (Monitor monitor : monitors) {
            monitor.execute(args);
        }
    }

    void installMonitor(Monitor monitor) {
        String[] evenSourceIds = monitor.evenSourceIds();
        for (String evenSourceId : evenSourceIds) {
            if (!this.monitorsMap.containsKey(evenSourceId)) {
                this.monitorsMap.putIfAbsent(evenSourceId, new HashSet<>());
            }
            this.monitorsMap.get(evenSourceId).add(monitor);
        }
    }
}
