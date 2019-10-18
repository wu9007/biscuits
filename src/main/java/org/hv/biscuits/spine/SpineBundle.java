package org.hv.biscuits.spine;

import org.hv.biscuits.bundle.AbstractBundle;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Component
public class SpineBundle extends AbstractBundle {

    @Override
    public void init() {
        this.register("guest", "Guest-Bundle");
    }
}
