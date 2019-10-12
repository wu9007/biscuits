package org.hv.biscuits.domain.process;

/**
 * @author wujianchuan
 */
public interface Context {

    /**
     * 创建流程作用域
     *
     * @param sortedNodeNames 排序后的流程节点名称
     * @throws Exception e
     */
    void setSortedNodeNames(String[] sortedNodeNames) throws Exception;

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
    void setCurrentNode(Node node);

    /**
     * 获取当前节点
     *
     * @return 当前节点
     */
    Node getCurrentNode();

    /**
     * 根据节点名称获取节点
     *
     * @param nodeName 节点名称
     * @return 节点
     */
    Node getNodeByName(String nodeName);

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
     * 将本作用域置为可用
     */
    void enable();

    /**
     * 将本作用域置为不可用
     */
    void disable();
}
