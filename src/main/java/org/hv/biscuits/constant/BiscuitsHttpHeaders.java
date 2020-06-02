package org.hv.biscuits.constant;

/**
 * @author leyan95
 */
public interface BiscuitsHttpHeaders {
    /**
     * 用户简称化名
     */
    String USER_AVATAR = "User-Avatar";
    /**
     * 用户名称
     */
    String USER_NAME = "User-Name";
    /**
     * 业务部门名称
     */
    String BUSINESS_DEPARTMENT_NAME = "Business-Department-Name";
    /**
     * 日志追链路标识
     */
    String TRACE_ID = "Trace-Id";
    /**
     * 请求标识
     */
    String REQUEST_ID = "Request-Id";
    /**
     * 工作单元执行标识
     */
    String SERVICE_ID = "Service-Id";
    /**
     * 消息事务标识
     */
    String TRANSACTION_ID = "Transaction-Id";
    /**
     * 仓储执行标识
     */
    String PERSISTENCE_ID = "Persistence-Id";
}
