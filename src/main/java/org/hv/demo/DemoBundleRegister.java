package org.hv.demo;

import org.hv.biscuits.bundle.AbstractBundle;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan 2019/2/26
 * @version 1.0
 */
@Component
public class DemoBundleRegister extends AbstractBundle {

    @Override
    public void init() {
        this.register("home", "主页");
        this.register("message", "消息");
        this.register("version", "版本");
        this.register("order", "订单");
        this.register("remote", "远程调用");
    }
}
