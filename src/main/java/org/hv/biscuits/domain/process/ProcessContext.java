package org.hv.biscuits.domain.process;

import java.util.Map;

/**
 * @author wujianchuan
 */
public class ProcessContext implements Context {
    /**
     * 业务数据的数据标识
     */
    private String dataUuid;
    /**
     * 通常保存后改流程才可用
     */
    private boolean enable = false;
    /**
     * 该流程中的节点名称和节点的映射
     */
    private Map<String, Node> nodeMapper;
    /**
     * 当前节点
     */
    private Node currentNode;

    ProcessContext(Map<String, Node> nodeMapper) {
        this.nodeMapper = nodeMapper;
    }

    @Override
    public void setSortedNodeNames(String[] sortedNodeNames) throws Exception {
        Node node = null;
        // 节点排序
        for (int index = 0; index < sortedNodeNames.length; index++) {
            Node preNode = node;
            node = this.getNodeByName(sortedNodeNames[index]);
            if (node == null) {
                throw new Exception(String.format("Can not find the node named <<%s>>", sortedNodeNames[index]));
            }
            node.setPreNode(preNode);

            if (index == 0) {
                this.setCurrentNode(node);
            }

            if (preNode != null) {
                preNode.setNextNode(node);
            }
        }
    }

    @Override
    public void setDataUuid(String dataUuid) {
        this.dataUuid = dataUuid;
    }

    @Override
    public String getDataUuid() {
        return this.dataUuid;
    }

    @Override
    public void setCurrentNode(Node node) {
        this.currentNode = node;
    }

    @Override
    public Node getNodeByName(String nodeName) {
        return this.nodeMapper.get(nodeName);
    }

    @Override
    public boolean accept() throws Exception {
        if (this.enable) {
            if (currentNode != null) {
                return this.currentNode.accept(this);
            } else {
                throw new Exception("Sorry, there are no more nodes.");
            }
        } else {
            throw new Exception("Accept with wrong state.");
        }
    }

    @Override
    public boolean rejection() throws Exception {
        if (!this.enable) {
            if (this.currentNode != null) {
                return this.currentNode.rejection(this);
            } else {
                throw new Exception("Sorry, there are no more nodes.");
            }
        } else {
            throw new Exception("Accept with wrong state.");
        }
    }

    @Override
    public void enable() {
        //TODO 将当前流程的信息持久化
        this.enable = true;
    }

    @Override
    public void disable() {
        //TODO 将当前流程信息从持久层数据中清除
        this.enable = false;
    }
}
