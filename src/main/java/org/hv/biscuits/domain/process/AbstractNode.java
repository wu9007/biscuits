package org.hv.biscuits.domain.process;

/**
 * @author wujianchuan
 */
public abstract class AbstractNode implements Node {
    private Node preNode;
    private Node nextNode;

    /**
     * 流程通过时执行的操作
     *
     * @return 是否执行成功
     */
    public abstract boolean doAccept();

    /**
     * 流程驳回时执行的操作
     *
     * @return 是否执行成功
     */
    public abstract boolean doRejection();

    @Override
    public void setPreNode(Node preNode) {
        this.preNode = preNode;
    }

    @Override
    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public boolean accept(Context context) {
        context.setCurrentNode(nextNode);
        return this.doAccept();
    }

    @Override
    public boolean rejection(Context context) {
        context.setCurrentNode(preNode);
        return this.doRejection();
    }
}
