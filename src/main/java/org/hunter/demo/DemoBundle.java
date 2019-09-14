package org.hunter.demo;

import org.hunter.skeleton.bundle.AbstractBundle;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Component
public class DemoBundle extends AbstractBundle {

    @Override
    public void init() {
        this.register("hunter", "主页");
    }
}
