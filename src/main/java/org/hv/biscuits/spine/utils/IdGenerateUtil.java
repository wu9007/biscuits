package org.hv.biscuits.spine.utils;

import org.hv.biscuits.core.session.ActiveSessionCenter;
import org.hv.biscuits.core.sn.IdGenerator;
import org.hv.biscuits.spine.model.BillType;
import org.hv.biscuits.spine.model.IdRule;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.exception.SessionException;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wujianchuan 2020/11/10 10:29
 */
@Component
public class IdGenerateUtil {
    @Value("${spring.application.name}")
    private String applicationName;
    private String serviceId;
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    @PostConstruct
    public void init() {
        serviceId = applicationName.replaceAll("\\d+", "");
    }

    /**
     * 生成单号
     *
     * @param billTypeCode 单据类型 对应手工维护在T_BILL_TYPE表中的数据
     * @param clazz        类类型
     * @param fieldName    字段名
     * @param deptSpell    不萌首字母缩写
     * @param strongSn     是否强连续（如果值为 true 业务数据保存或报错后对应的调用单号生成工具的提交和回滚方法）
     * @param sessionName  数据库会话名
     * @return 单号
     * @throws Exception e
     */
    public String generate(String billTypeCode, Class<? extends AbstractEntity> clazz, String fieldName, String deptSpell, boolean strongSn, String sessionName) throws Exception {
        IdRule idRule = this.getRule(billTypeCode, sessionName);
        String clientType = idRule.getClientType();
        String preCode = "";
        if (clientType != null) {
            preCode = clientType;
        }
        preCode += idRule.getBillCode();
        String dateFormatter = idRule.getDateFormatter();
        if (idRule.getDiffByDept()) {
            preCode += deptSpell;
        }
        return idGenerator.generate(sessionName, clazz, fieldName, preCode, dateFormatter, idRule.getSerialLength(), idRule.getResetRule(), strongSn);
    }

    /**
     * 单号跟随业务数据的持久化事务的提交而提交
     *
     * @param billTypeCode 单据类型
     * @param deptSpell    部门首字母缩写
     * @param sessionName  数据库会话名
     */
    public void commit(String billTypeCode, String deptSpell, String sessionName) {
        IdRule idRule = this.getRule(billTypeCode, sessionName);
        String clientType = idRule.getClientType();
        String preCode = "";
        if (clientType != null) {
            preCode = clientType;
        }
        preCode += idRule.getBillCode();
        if (idRule.getDiffByDept()) {
            preCode += deptSpell;
        }
        idGenerator.commit(preCode);
    }

    /**
     * 单号跟随业务数据的持久化事务的回滚而回滚
     *
     * @param billTypeCode 单据类型
     * @param deptSpell    部门首字母缩写
     * @param sessionName  数据库会话名
     */
    public void rollback(String billTypeCode, String deptSpell, String sessionName) {
        IdRule idRule = this.getRule(billTypeCode, sessionName);
        String clientType = idRule.getClientType();
        String preCode = "";
        if (clientType != null) {
            preCode = clientType;
        }
        preCode += idRule.getBillCode();
        if (idRule.getDiffByDept()) {
            preCode += deptSpell;
        }
        idGenerator.rollback(preCode);
    }

    private IdRule getRule(String billTypeCode, String sessionName) {
        Session newSession = SessionFactory.getSession(sessionName);
        if (newSession == null) {
            throw new SessionException("请正确开启数据库会话");
        }
        newSession.open();
        // 去掉系统区分条件，给billType添加唯一索引  .add(Restrictions.equ("serviceId", serviceId))
        IdRule idRule = newSession.createCriteria(IdRule.class).add(Restrictions.equ("billType", billTypeCode)).unique();
        if (idRule == null) {
            BillType billType = newSession.createCriteria(BillType.class).add(Restrictions.equ("code", billTypeCode)).unique();
            if (billType == null) {
                throw new IllegalArgumentException(String.format("找不到 bill type code 为 %s 的单号生成规则", billTypeCode));
            } else {
                throw new IllegalArgumentException(String.format("请先维护【%s】单据信息。", billType.getName()));
            }
        }
        newSession.close();
        return idRule;
    }
}
