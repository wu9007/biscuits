package org.hv.biscuits.spine.identify;

/**
 * @author wujianchuan
 */
public interface BisGenerationType {
    /**
     * 【4位-实例标识（避免多实例向同一个表中插入数据时发生主键冲突）】+【4位-表标识】+【5位-站标识】+【8位-年月日】+【6位-序列号】
     */
    String SERVER_TABLE_STATION_DATE_INCREMENT = "serverTableStationDateIncrement";
}
