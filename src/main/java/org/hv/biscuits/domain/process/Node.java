package org.hv.biscuits.domain.process;

public interface Node {

    /**
     * 设置上一个节点
     *
     * @param preNode 上一个节点
     */
    void setPreNode(Node preNode);

    /**
     * 设置下一个节点
     *
     * @param nextNode 下一个节点
     */
    void setNextNode(Node nextNode);

    /**
     * 流程通过时执行的操作
     *
     * @param context 流程作用域
     * @return 是否执行成功
     */
    boolean accept(Context context);

    /**
     * 流程驳回时执行的操作
     *
     * @param context 流程作用域
     * @return 是否执行成功
     */
    boolean rejection(Context context);
}
