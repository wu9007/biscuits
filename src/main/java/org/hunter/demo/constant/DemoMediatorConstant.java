package org.hunter.demo.constant;

import org.hunter.skeleton.mediator.Mediator;

/**
 * @author wujianchuan
 */
public interface DemoMediatorConstant extends Mediator {
    /**
     * update the status of {@link org.hunter.demo.model.RelevantBill}
     */
    String MEDIATOR_UPDATE_RELEVANT_STATUS = "DM-001";
    /**
     * find order by uuid {@link org.hunter.demo.model.Order}
     */
    String MEDIATOR_FIND_ORDER_BY_UUID= "DM-002";
}
