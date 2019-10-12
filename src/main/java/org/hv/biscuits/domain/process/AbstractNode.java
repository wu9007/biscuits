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
     * @param dataUuid 业务数据的数据标识
     * @return 是否执行成功
     */
    public abstract boolean doAccept(String dataUuid);

    /**
     * 流程驳回时执行的操作
     *
     * @param dataUuid 业务数据的数据标识
     * @return 是否执行成功
     */
    public abstract boolean doRejection(String dataUuid);

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
        return this.doAccept(context.getDataUuid());
    }

    @Override
    public boolean rejection(Context context) {
        context.setCurrentNode(preNode);
        return this.doRejection(context.getDataUuid());
    }
}
