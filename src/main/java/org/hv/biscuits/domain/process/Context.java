package org.hv.biscuits.domain.process;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface Context {

    /**
     * 设置业务数据的数据标识
     *
     * @param dataUuid 业务数据的数据标识
     */
    void setDataUuid(String dataUuid);

    /**
     * 获取业务数据的数据标识
     *
     * @return 业务数据的数据标识
     */
    String getDataUuid();

    /**
     * 设置当前节点
     *
     * @param node 节点
     */
    void setCurrentNode(Node node) throws SQLException;

    /**
     * 获取当前节点
     *
     * @return 当前节点
     */
    Node getCurrentNode();

    /**
     * 根据节点名称获取节点
     *
     * @param nodeIdentify 节点标识
     * @return 节点
     */
    Node getNodeByIdentify(String nodeIdentify);

    /**
     * 流程通过
     *
     * @return 是否执行成功
     * @throws Exception e
     */
    boolean accept() throws Exception;

    /**
     * 流程驳回
     *
     * @return 是否执行成功
     * @throws Exception e
     */
    boolean rejection() throws Exception;

    /**
     * 将本作用域置为可用同时向数据库中保存数据
     */
    void enable() throws SQLException;

    /**
     * 设置可用性
     *
     * @param enable 是否可用
     */
    void setEnable(boolean enable);

    /**
     * 将本作用域置为不可用同时删除数据库中的数据
     */
    void disable() throws SQLException;
}
