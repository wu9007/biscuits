package org.hv.demo;

import org.hv.biscuits.permission.AbstractPermission;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan 2019/2/2
 */
@Component
public class DemoPermission extends AbstractPermission {

    @Override
    public void init() {
        super.register("order", "order_read", "读取", "读取订单数据的权限");
        super.register("order", "order_manage", "管理", "管理订单数据的权限");
    }
}
