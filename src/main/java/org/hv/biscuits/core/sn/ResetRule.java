package org.hv.biscuits.core.sn;

/**
 * 单号清零规则
 *
 * @author wujianchuan 2020/11/8 13:53
 */
public interface ResetRule {
    /**
     * 一年清一次
     */
    String YEAR = "y";
    /**
     * 一个月清一次
     */
    String MONTH = "M";
    /**
     * 一天清一次
     */
    String DAY = "d";
}
