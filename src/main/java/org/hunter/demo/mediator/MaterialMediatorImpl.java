package org.hunter.demo.mediator;

import org.hunter.demo.service.OrderService;
import org.hunter.demo.service.RelevantBillService;
import org.hunter.demo.service.impl.RelevantBillServiceImpl;
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
        super.installService(OrderService.class);
        super.installService(RelevantBillService.class);

        super.installBusiness(UPDATE_RELEVANT_BILL_STATUS, (RelevantBillServiceImpl service, Map<String, Object> args) -> {
            String uuid = (String) args.get("uuid");
            boolean status = (boolean) args.get("available");
            service.updateStatus(uuid, status);
            return null;
        });
    }

    @Override
    public Object call(String businessName, Map<String, Object> args) throws Exception {
        return super.apply(businessName, RelevantBillServiceImpl.class, args);
    }
}
