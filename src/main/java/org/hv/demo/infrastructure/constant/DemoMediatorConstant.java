package org.hv.demo.infrastructure.constant;

import org.hv.biscuits.domain.mediator.Mediator;
import org.hv.demo.bundles.bundle_order.aggregate.Order;
import org.hv.demo.bundles.bundle_relevant.aggregate.RelevantBill;

/**
 * @author wujianchuan
 */
public interface DemoMediatorConstant extends Mediator {
    /**
     * update the status of {@link RelevantBill}
     */
    String MEDIATOR_UPDATE_RELEVANT_STATUS = "DM-001";
    /**
     * find order by uuid {@link Order}
     */
    String MEDIATOR_FIND_ORDER_BY_UUID= "DM-002";
}
