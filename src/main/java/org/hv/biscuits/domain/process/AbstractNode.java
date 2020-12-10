package org.hv.biscuits.domain.process;

/**
 * @author leyan95
 */
public abstract class AbstractNode implements Node {
    private Node preNode;
    private Node nextNode;
    private boolean isTop;
    private boolean isTail;

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

    /**
     * 流程驳回到初始状态时执行的操作
     *
     * @param dataUuid 业务数据的数据标识
     * @return 是否执行成功
     */
    public abstract boolean doRejectionToInitial(String dataUuid) throws Exception;

    @Override
    public void setPreNode(Node preNode) {
        this.preNode = preNode;
    }

    @Override
    public Node getPreNode() {
        return this.preNode;
    }

    @Override
    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public Node getNextNode() {
        return this.nextNode;
    }

    @Override
    public void beTop() {
        this.isTop = true;
    }

    @Override
    public boolean isTop() {
        return this.isTop;
    }

    @Override
    public void beTail() {
        this.isTail = true;
    }

    @Override
    public boolean isTail() {
        return this.isTail;
    }

    @Override
    public boolean accept(Context context) throws Exception {
        boolean success = this.doAccept(context.getDataUuid());
        if (success) {
            context.setCurrentNode(nextNode);
        }
        return success;
    }

    @Override
    public boolean rejection(Context context) throws Exception {
        boolean success = this.doRejection(context.getDataUuid());
        if (success) {
            context.setCurrentNode(preNode);
        }
        return success;
    }

    @Override
    public boolean rejectionToInitial(Context context) throws Exception {
        boolean success = this.doRejectionToInitial(context.getDataUuid());
        if (success) {
            context.setCurrentNode(context.getFirstNode());
        }
        return success;
    }
}
