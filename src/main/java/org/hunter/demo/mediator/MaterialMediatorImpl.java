package org.hunter.demo.mediator;

import org.hunter.demo.service.RelevantBillService;
import org.hunter.skeleton.mediator.AbstractMediator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wujianchuan
 */
@Component
public class MaterialMediatorImpl extends AbstractMediator implements MaterialMediator {

    @Override
    protected void init() {
        // Update relevant bill available state.
        super.installBusiness(UPDATE_RELEVANT_BILL_STATUS, RelevantBillService.class, (RelevantBillService service, Map<String, Object> args) -> {
            String uuid = (String) args.get("uuid");
            boolean status = (boolean) args.get("available");
            service.updateStatus(uuid, status);
            return null;
        });
        // Install other business functions witch you need in the future.
    }

    @Override
    public Object call(String businessName, Map<String, Object> args) throws Exception {
        return super.apply(businessName, args);
    }
}
