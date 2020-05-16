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

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccessorName() {
        return accessorName;
    }

    public void setAccessorName(String accessorName) {
        this.accessorName = accessorName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getInParameter() {
        return inParameter;
    }

    public void setInParameter(String inParameter) {
        this.inParameter = inParameter;
    }

    public String getOutParameter() {
        return outParameter;
    }

    public void setOutParameter(String outParameter) {
        this.outParameter = outParameter;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBusinessDeptName() {
        return businessDeptName;
    }

    public void setBusinessDeptName(String businessDeptName) {
        this.businessDeptName = businessDeptName;
    }
}
