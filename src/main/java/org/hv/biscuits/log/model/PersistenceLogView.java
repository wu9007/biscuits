package org.hv.biscuits.log.model;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author leyan95
 */
public class PersistenceLogView implements Serializable {
    private static final long serialVersionUID = -7860562014778235746L;
    /**
     * 在请求到达工作单元切面时，此项被放在请求属性上。
     */
    private String serviceId;
    /**
     * 在请求到达控制器切面时，此项被放在请求属性上,
     * 当系统通过 BisRocketMqTemplate 发送事务消息时，取请求属性中的事务标识并放到MQ消息头中。
     */
    private String globalTransactionId;
    /**
     * 再请求到达仓储切面是，此项被放在请求属性上
     */
    private String persistenceId;
    private String persistenceName;
    private String methodName;
    private String inParameter;
    private String outParameter;
    private String exception;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public PersistenceLogView() {
        this.startDateTime = LocalDateTime.now();
    }

    public PersistenceLogView(String serviceId) {
        this.startDateTime = LocalDateTime.now();
        this.serviceId = serviceId;
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public PersistenceLogView setGlobalTransactionId(String globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public PersistenceLogView setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getPersistenceId() {
        return persistenceId;
    }

    public PersistenceLogView setPersistenceId(String persistenceId) {
        this.persistenceId = persistenceId;
        return this;
    }

    public String getPersistenceName() {
        return persistenceName;
    }

    public PersistenceLogView setPersistenceName(String persistenceName) {
        this.persistenceName = persistenceName;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public PersistenceLogView setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String getInParameter() {
        return inParameter;
    }

    public PersistenceLogView setInParameter(String inParameter) {
        this.inParameter = inParameter;
        return this;
    }

    public String getOutParameter() {
        return outParameter;
    }

    public PersistenceLogView setOutParameter(String outParameter) {
        this.outParameter = outParameter;
        return this;
    }

    public String getException() {
        return exception;
    }

    public PersistenceLogView setException(String exception) {
        this.exception = exception;
        return this;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public PersistenceLogView setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public PersistenceLogView setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }
}
