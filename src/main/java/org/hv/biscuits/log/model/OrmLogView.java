package org.hv.biscuits.log.model;

import org.hv.biscuits.message.BisRocketMqTemplate;
import org.hv.pocket.logger.view.PersistenceMirrorView;

import java.io.Serializable;

/**
 * @author leyan95
 */
public class OrmLogView implements Serializable {
    private static final long serialVersionUID = -7860562014778235746L;
    /**
     * 在请求到达控制器切面时，此项被放在请求属性上,
     * 当系统通过{@link BisRocketMqTemplate}发送事务消息时，取请求属性中的事务标识并放到MQ消息头中。
     */
    private String globalTransactionId;
    /**
     * 再请求到达仓储切面是，此项被放在请求属性上
     */
    private String persistenceId;
    private PersistenceMirrorView persistenceMirrorView;

    public OrmLogView() {
    }

    public OrmLogView(String globalTransactionId, String persistenceId, PersistenceMirrorView persistenceMirrorView) {
        this.globalTransactionId = globalTransactionId;
        this.persistenceId = persistenceId;
        this.persistenceMirrorView = persistenceMirrorView;
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public OrmLogView setGlobalTransactionId(String globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
        return this;
    }

    public String getPersistenceId() {
        return persistenceId;
    }

    public OrmLogView setPersistenceId(String persistenceId) {
        this.persistenceId = persistenceId;
        return this;
    }

    public PersistenceMirrorView getPersistenceMirrorView() {
        return persistenceMirrorView;
    }

    public OrmLogView setPersistenceMirrorView(PersistenceMirrorView persistenceMirrorView) {
        this.persistenceMirrorView = persistenceMirrorView;
        return this;
    }
}
