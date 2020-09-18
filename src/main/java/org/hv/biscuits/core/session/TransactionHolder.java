package org.hv.biscuits.core.session;

import org.hv.pocket.session.Session;
import org.hv.pocket.session.Transaction;

/**
 * 负责数据库会话中事务的创建、开启、关闭以及异常处理
 *
 * @author wujianchuan 2020/9/10 10:28
 */
public class TransactionHolder {
    /**
     * 每个线程开启的数据库事务
     */
    private static final ThreadLocal<Transaction> TRANSACTION_THREAD_LOCAL = new ThreadLocal<>();
    /**
     * 每个线程当前所在方法（以带由@Affair注解且on为true）的方法为起始节点，当线程所在方法节点序号再次变为0，则表示关闭数据库事务
     */
    private static final ThreadLocal<Integer> METHOD_NODE_NUMBER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 在使用ActiveSession注册后执行事务前置处理
     *
     * @param session 数据库绘画
     * @param enable  是否开启事务
     */
    protected static void preProcess(Session session, boolean enable) {
        if (enable) {
            Transaction transaction = TRANSACTION_THREAD_LOCAL.get();
            if (transaction == null) {
                transaction = session.getTransaction();
                transaction.begin();
                TRANSACTION_THREAD_LOCAL.set(transaction);
                METHOD_NODE_NUMBER_THREAD_LOCAL.set(0);
            } else {
                int methodNodeNumber = METHOD_NODE_NUMBER_THREAD_LOCAL.get();
                METHOD_NODE_NUMBER_THREAD_LOCAL.set(methodNodeNumber + 1);
            }
        }
    }

    /**
     * 在使用ActiveSession取消注册后执行事务的后置处理
     */
    protected static void proProcess(boolean enable) {
        if (enable) {
            Transaction transaction = TRANSACTION_THREAD_LOCAL.get();
            if (transaction != null) {
                int methodNodeNumber = METHOD_NODE_NUMBER_THREAD_LOCAL.get();
                if (methodNodeNumber == 0) {
                    transaction.commit();
                    remove();
                } else {
                    METHOD_NODE_NUMBER_THREAD_LOCAL.set(methodNodeNumber - 1);
                }
            }
        }
    }

    /**
     * 在使用ActiveSession处理异常时执行事务的异常处理
     */
    protected static void exceptionProcess(boolean enable) {
        if (enable) {
            int methodNodeNumber = METHOD_NODE_NUMBER_THREAD_LOCAL.get();
            if (methodNodeNumber == 0) {
                Transaction transaction = TRANSACTION_THREAD_LOCAL.get();
                transaction.rollBack();
                remove();
            } else {
                METHOD_NODE_NUMBER_THREAD_LOCAL.set(methodNodeNumber - 1);
            }
        }
    }

    /**
     * 清理资源
     */
    private static void remove() {
        METHOD_NODE_NUMBER_THREAD_LOCAL.remove();
        TRANSACTION_THREAD_LOCAL.remove();
    }
}
