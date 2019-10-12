package org.hv.biscuits.launcher;

import org.hv.biscuits.domain.even.EvenCenter;
import org.hv.biscuits.domain.even.Monitor;
import org.hv.biscuits.utils.AopTargetUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wujianchuan
 */
@Component
@Order(4)
public class EvenLauncher implements CommandLineRunner {

    private final List<Monitor> monitors;

    public EvenLauncher(@Nullable List<Monitor> monitors) {
        this.monitors = monitors;
    }


    @Override
    public void run(String... args) throws Exception {
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitorProxy : monitors) {
                Object target = AopTargetUtils.getTarget(monitorProxy);
                // install monitor
                if (target instanceof Monitor) {
                    EvenCenter.getInstance().installMonitor(monitorProxy);
                }
            }
        }
    }
}
