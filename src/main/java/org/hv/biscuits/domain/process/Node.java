package org.hv.biscuits.domain.process;

/**
 * @author leyan95
 */
public interface Node {

    /**
     * 获取节点标识
     *
     * @return 节点标识
     */
    String getIdentify();

    /**
     * 节点名称
     *
     * @return 节点名称
     */
    String getSurName();

    /**
     * 将该节点设置为头节点
     */
    void beTop();

    /**
     * 获取该节点是否为头节点
     *
     * @return 是否为头节点
     */
    boolean isTop();

    /**
     * 将该节点设置为尾节点
     */
    void beTail();

    /**
     * 获取该节点是否为尾节点
     *
     * @return 是否为尾节点
     */
    boolean isTail();

    /**
     * 设置上一个节点
     *
     * @param preNode 上一个节点
     */
    void setPreNode(Node preNode);

    /**
     * 获取前一个结点
     *
     * @return 前一个结点
     */
    Node getPreNode();

    /**
     * 设置下一个节点
     *
     * @param nextNode 下一个节点
     */
    void setNextNode(Node nextNode);

    /**
     * 获取下一个节点
     *
     * @return 下一个节点
     */
    Node getNextNode();

    /**
     * 通过
     *
     * @param context 流程作用域
     * @return 是否执行成功
     */
    boolean accept(Context context) throws Exception;

    /**
     * 驳回到前一个状态
     *
     * @param context 流程作用域
     * @return 是否执行成功
     */
    boolean rejection(Context context) throws Exception;

    /**
     * 驳回到初始状态
     *
     * @param context 流程作用域
     * @return 是否执行成功
     */
    boolean rejectionToInitial(Context context) throws Exception;
}
