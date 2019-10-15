package org.hv.biscuits.domain.process;

import org.hv.biscuits.spine.model.Process;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Modern;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author wujianchuan
 */
public class ProcessContext implements Context {
    private final Session session = SessionFactory.getSession("biscuits");
    /**
     * 流程标识
     */
    private String processIdentify;
    /**
     * 业务数据的数据标识
     */
    private String dataUuid;
    /**
     * 指定的流程顺序
     */
    private String[] sortedNodeNames;
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

    ProcessContext(String processIdentify, String dataUuid, String[] sortedNodeNames, Map<String, Node> nodeMapper) throws Exception {
        this.processIdentify = processIdentify;
        this.dataUuid = dataUuid;
        this.nodeMapper = nodeMapper;
        this.sortedNodeNames = sortedNodeNames;
        this.init();
    }

    public void init() throws Exception {
        Node node = null;
        // 节点排序
        for (int index = 0; index < this.sortedNodeNames.length; index++) {
            Node preNode = node;
            node = this.getNodeByIdentify(this.sortedNodeNames[index]);
            if (node == null) {
                throw new Exception(String.format("Can not find the node named <<%s>>", this.sortedNodeNames[index]));
            }
            node.setPreNode(preNode);

            if (index == 0) {
                node.beTop();
                this.setCurrentNode(node);
            } else if (index == this.sortedNodeNames.length - 1) {
                node.beTail();
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
    public void setCurrentNode(Node node) throws SQLException {
        this.currentNode = node;
        this.session.open();
        Criteria criteria = this.session.createCriteria(Process.class);
        criteria.add(Restrictions.equ("processId", this.processIdentify))
                .add(Restrictions.equ("dataUuid", this.dataUuid))
                .add(Modern.set("currentNodeId", node.getIdentify()));
        criteria.update();
        this.session.close();
    }

    @Override
    public Node getCurrentNode() {
        return this.currentNode;
    }

    @Override
    public Node getFirstNode() {
        return this.getNodeByIdentify(this.sortedNodeNames[0]);
    }

    @Override
    public Node getNodeByIdentify(String nodeIdentify) {
        return this.nodeMapper.get(nodeIdentify);
    }

    @Override
    public boolean accept() throws Exception {
        if (this.enable) {
            if (currentNode != null && !currentNode.isTail()) {
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
        if (this.enable) {
            if (this.currentNode != null && !currentNode.isTop()) {
                return this.currentNode.rejection(this);
            } else {
                throw new Exception("Sorry, there are no more nodes.");
            }
        } else {
            throw new Exception("Accept with wrong state.");
        }
    }

    @Override
    public boolean rejectionInitial() throws Exception {
        if (this.enable) {
            return this.currentNode.rejectionToInitial(this);
        } else {
            throw new Exception("Accept with wrong state.");
        }
    }

    @Override
    public void enable() throws SQLException {
        // 将当前流程的信息持久化
        Process process = Process.newInstance(this.processIdentify, this.dataUuid, this.currentNode.getIdentify(), this.sortedNodeNames);
        this.session.open();
        this.session.save(process);
        this.session.close();
        this.setEnable(true);
    }

    @Override
    public void disable() throws SQLException {
        // 将当前流程信息从持久层数据中清除
        this.session.open();
        Criteria criteria = this.session.createCriteria(Process.class);
        criteria.add(Restrictions.equ("processId", this.processIdentify))
                .add(Restrictions.equ("dataUuid", this.dataUuid));
        criteria.delete();
        this.session.close();
        this.setEnable(false);
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
