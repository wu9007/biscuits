package org.hv.biscuits.core;

import org.hv.biscuits.core.views.BundleView;
import org.hv.biscuits.core.views.PermissionView;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author wujianchuan
 */
public interface BiscuitsConfig {
    /**
     * 结构初始化
     *
     * @param withPersistence 是否在系统中使用持久化相关功能
     * @return biscuits config
     * @throws Exception e
     */
    BiscuitsConfig init(boolean withPersistence) throws Exception;

    /**
     * 获取映射信息
     *
     * @return 映射信息
     */
    Map<String, BundleView> getActionMap();

    /**
     * 获取权限信息
     *
     * @return 权限信息
     */
    Map<String, PermissionView> getPermissionMap();

    /**
     * 根据运行环境对映射信息进行持久化操作
     *
     * @param serviceId                 服务标识
     * @param actionMap                 映射信息
     * @return biscuits config
     * @throws SQLException e
     */
    BiscuitsConfig persistenceMapper(String serviceId, Map<String, BundleView> actionMap) throws SQLException;

    /**
     * 根据运行环境对权限信息进行持久化操作
     *
     * @param serviceId                 服务标识
     * @param permissionMap             权限信息
     * @return biscuits config
     * @throws SQLException e
     */
    BiscuitsConfig resetPersistencePermission(String serviceId, Map<String, PermissionView> permissionMap) throws SQLException;

    /**
     * 讲当前运行环境设置成开发环境
     *
     * @return biscuits config
     */
    BiscuitsConfig runWithDevelopEnvironment();
}
