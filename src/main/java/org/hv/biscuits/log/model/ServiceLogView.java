package org.hv.biscuits.log.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author leyan95
 */
public class ServiceLogView implements Serializable {
    private static final long serialVersionUID = -7860562014778235746L;
    /**
     * 在请求到达控制器切面时，此项被放在请求属性上。
     */
    private String requestId;
    /**
     * 在请求到达工作单元切面时，此项被放在请求属性上。
     */
    private String serviceId;
    /**
     * 在请求到达控制器切面时，此项被放在请求属性上,
     * 当系统通过 BisRocketMqTemplate 发送事务消息时，取请求属性中的事务标识并放到MQ消息头中。
     */
    private String globalTransactionId;
    private String serviceName;
    private String methodName;
    private String inParameter;
    private String outParameter;
    private String exception;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public ServiceLogView() {
        this.startDateTime = LocalDateTime.now();
    }

    public ServiceLogView(String requestId) {
        this.startDateTime = LocalDateTime.now();
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public ServiceLogView setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public ServiceLogView setGlobalTransactionId(String globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public ServiceLogView setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceLogView setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public ServiceLogView setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String getInParameter() {
        return inParameter;
    }

    public ServiceLogView setInParameter(String inParameter) {
        this.inParameter = inParameter;
        return this;
    }

    public String getOutParameter() {
        return outParameter;
    }

    public ServiceLogView setOutParameter(String outParameter) {
        this.outParameter = outParameter;
        return this;
    }

    public String getException() {
        return exception;
    }

    public ServiceLogView setException(String exception) {
        this.exception = exception;
        return this;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public ServiceLogView setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public ServiceLogView setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }
}
