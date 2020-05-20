package org.hv.demo.bundles.bundle_order.service;

import org.hv.biscuits.annotation.Affairs;
import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.controller.FilterView;
import org.hv.biscuits.domain.even.EvenCenter;
import org.hv.biscuits.domain.process.Context;
import org.hv.biscuits.domain.process.ContextFactory;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.service.PageList;
import org.hv.demo.bundles.bundle_order.aggregate.Order;
import org.hv.demo.bundles.bundle_order.repository.OrderRepository;
import org.hv.demo.bundles.bundle_relevant.service.RelevantBillService;
import org.hv.demo.infrastructure.mediator.OrderMediator;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

import static org.hv.demo.infrastructure.constant.DemoEvenConstant.EVEN_ORDER_SAVE;
import static org.hv.demo.infrastructure.constant.DemoEvenConstant.EVEN_ORDER_UPDATE;
import static org.hv.demo.infrastructure.constant.DemoMediatorConstant.MEDIATOR_UPDATE_RELEVANT_STATUS;

/**
 * @author leyan95
 */
@Service(session = "demo")
public class OrderServiceImpl extends AbstractService implements OrderService {

    private final OrderRepository orderRepository;
    private final RelevantBillService relevantBillService;
    private final OrderMediator orderMediator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderMediator orderMediator, RelevantBillService relevantBillService) {
        this.orderRepository = orderRepository;
        this.orderMediator = orderMediator;
        this.relevantBillService = relevantBillService;
    }

    @Override
    @Affairs
    public int save(Order order) throws Exception {
        // Call Mediator to check and change relevant bill status.
        this.orderMediator.call(MEDIATOR_UPDATE_RELEVANT_STATUS, order.getRelevantBillUuid(), false);

        // When transactions are nested, only the outermost transactions are opened.
        this.relevantBillService.findOne(order.getRelevantBillUuid());

        // Execute own job
        int numberOfRowsAffected = this.orderRepository.saveWithTrack(order, true, "ADMIN", "保存订单");

        // Call monitors
        EvenCenter.getInstance().fireEven(EVEN_ORDER_SAVE, numberOfRowsAffected, order.getUuid(), "save");

        // Build process context and Set to available and set initial function.
        Context auditProcessContext = ContextFactory.getInstance().buildProcessContext(
                "orderAuditProcessor",
                order.getUuid(),
                new String[]{"directLeaderAuditNode", "departmentAuditNode", "groupAuditNode"}
        );
        auditProcessContext.enable();

        return numberOfRowsAffected;
    }

    @Override
    @Affairs
    public int update(Order order) throws Exception {
        int numberOfRowsAffected = this.orderRepository.updateWithTrack(order, true, "ADMIN", "更新订单");

        // Call monitors
        EvenCenter.getInstance().fireEven(EVEN_ORDER_UPDATE, numberOfRowsAffected, order.getUuid(), "update");
        return numberOfRowsAffected;
    }

    @Override
    public boolean audit(String uuid) throws Exception {
        Context auditProcessContext = ContextFactory.getInstance().getProcessContext("orderAuditProcessor", uuid);
        if (auditProcessContext.getCurrentNode().isTail()) {
            throw new Exception("该节点为最后一个节点故无法再进行审核。");
        }
        return auditProcessContext.accept();
    }

    @Override
    public boolean rejectionPreviousNode(String uuid) throws Exception {
        Context auditProcessContext = ContextFactory.getInstance().getProcessContext("orderAuditProcessor", uuid);
        if (auditProcessContext.getCurrentNode().isTop()) {
            throw new Exception("该节点为第一个节点故无法再进行撤销。");
        }
        return auditProcessContext.rejection();
    }

    @Override
    public boolean rejectionInitialNode(String uuid) throws Exception {
        Context auditProcessContext = ContextFactory.getInstance().getProcessContext("orderAuditProcessor", uuid);
        if (auditProcessContext.getCurrentNode().isTop()) {
            throw new Exception("该节点为第一个节点故无法再进行撤销。");
        }
        return auditProcessContext.rejectionInitial();
    }

    @Override
    public Order findOne(String uuid) throws SQLException {
        return this.orderRepository.findOne(uuid);
    }

    @Override
    public PageList loadPageList(FilterView filterView) throws SQLException {
        return this.orderRepository.loadPage(filterView);
    }
}
