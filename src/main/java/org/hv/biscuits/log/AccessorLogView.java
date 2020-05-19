package org.hv.biscuits.log;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wujianchuan
 */
public class AccessorLogView implements Serializable {
    private static final long serialVersionUID = -7860562014778235746L;
    private String traceId;
    private String requestId;
    private String accessorName;
    private String methodName;
    private String inParameter;
    private String outParameter;
    private String exception;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String userName;
    private String businessDeptName;

    public AccessorLogView() {
        this.startDateTime = LocalDateTime.now();
    }

    public AccessorLogView(String traceId, String requestId, String userName) {
        this.startDateTime = LocalDateTime.now();
        this.traceId = traceId;
        this.requestId = requestId;
        this.userName = userName;
    }

    public AccessorLogView(String traceId, String requestId, String userName, String businessDeptName) {
        this.startDateTime = LocalDateTime.now();
        this.traceId = traceId;
        this.requestId = requestId;
        this.userName = userName;
        this.businessDeptName = businessDeptName;
    }

    public String getTraceId() {
        return traceId;
    }

    public AccessorLogView setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public AccessorLogView setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getAccessorName() {
        return accessorName;
    }

    public AccessorLogView setAccessorName(String accessorName) {
        this.accessorName = accessorName;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public AccessorLogView setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String getInParameter() {
        return inParameter;
    }

    public AccessorLogView setInParameter(String inParameter) {
        this.inParameter = inParameter;
        return this;
    }

    public String getOutParameter() {
        return outParameter;
    }

    public AccessorLogView setOutParameter(String outParameter) {
        this.outParameter = outParameter;
        return this;
    }

    public String getException() {
        return exception;
    }

    public AccessorLogView setException(String exception) {
        this.exception = exception;
        return this;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public AccessorLogView setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public AccessorLogView setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public AccessorLogView setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getBusinessDeptName() {
        return businessDeptName;
    }

    public AccessorLogView setBusinessDeptName(String businessDeptName) {
        this.businessDeptName = businessDeptName;
        return this;
    }
}
