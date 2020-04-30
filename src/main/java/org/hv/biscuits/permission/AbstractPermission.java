package org.hv.biscuits.permission;


import org.hv.biscuits.core.ActionFactory;

/**
 * @author wujianchuan 2019/2/25
 */
public abstract class AbstractPermission implements Permission {

    protected void register(String authId, String name, String common) {
        ActionFactory.register(authId, name, common);
    }
}
