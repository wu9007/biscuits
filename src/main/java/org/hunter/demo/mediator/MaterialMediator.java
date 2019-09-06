package org.hunter.demo.mediator;

import org.hunter.skeleton.mediator.Mediator;

public interface MaterialMediator extends Mediator {
    /**
     * update the status of {@link org.hunter.demo.model.RelevantBill}
     */
    String UPDATE_RELEVANT_BILL_STATUS = "updateRelevantBillStatus";
}
