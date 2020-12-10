package org.hv.biscuits.permission;


import org.hv.biscuits.core.ActionHolder;

/**
 * @author leyan95 2019/2/25
 */
public abstract class AbstractPermission implements Permission {

    protected void register(String authId, String name, String common) {
        ActionHolder.register(authId, name, common);
    }
}
