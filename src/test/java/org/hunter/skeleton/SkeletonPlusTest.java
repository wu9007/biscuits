package org.hunter.skeleton;

import org.hunter.Application;
import org.hunter.demo.model.Order;
import org.hunter.demo.model.RelevantBill;
import org.hunter.demo.model.RelevantBillDetail;
import org.hunter.demo.service.OrderPlusService;
import org.hunter.demo.service.RelevantBillService;
import org.hunter.pocket.model.BaseEntity;
import org.hunter.skeleton.controller.Filter;
import org.hunter.skeleton.controller.FilterView;
import org.hunter.skeleton.service.PageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wujianchuan 2019/1/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SkeletonPlusTest {
    @Autowired
    OrderPlusService orderService;
    @Autowired
    RelevantBillService relevantBillService;

    @Test
    public void test1() throws SQLException, IllegalAccessException {
        Order order = new Order();
        order.setCode("C-001");
        order.setState(null);
        order.setType("001");
        order.setTime(LocalDateTime.now());
        order.setPrice(new BigDecimal("99.56789"));
        order.setDay(LocalDate.now());
        this.orderService.save(order);
    }

    @Test
    public void test2() {
        List<RelevantBillDetail> commodities = new ArrayList<>();
        IntStream.range(1, 20).forEach((index) -> {
            RelevantBillDetail commodity = new RelevantBillDetail();
            commodity.setUuid(String.valueOf(index));
            commodity.setName("c1");
            commodity.setType("001");
            commodity.setPrice(new BigDecimal("11.2"));
            commodities.add(commodity);
        });
        // 聚合为LikedHashMap
        Map<String, RelevantBillDetail> result = commodities.stream().collect(Collectors.toMap(BaseEntity::getUuid, commodity -> commodity, (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, LinkedHashMap::new));
        System.out.println(result.size());
    }

    @Test
    public void test3() throws SQLException {
        FilterView filterView = new FilterView();
        filterView.setStart(0);
        filterView.setLimit(10);
        List<Filter> filters = new LinkedList<>();
        filters.add(filterView.createFilter("code", "C-001", FilterView.Operate.EQU));
        filters.add(filterView.createFilter("price", 500.0, FilterView.Operate.LT));
        filterView.setFilters(filters);

        PageList pageList = this.orderService.loadPageList(filterView);
        System.out.println(pageList.getCount());
    }

    @Test
    public void test4() throws SQLException, IllegalAccessException {
        List<RelevantBillDetail> details = new ArrayList<>();
        IntStream.range(1, 20).forEach((index) -> {
            RelevantBillDetail detail = new RelevantBillDetail();
            detail.setName("c1");
            detail.setType("001");
            detail.setPrice(new BigDecimal("11.2"));
            details.add(detail);
        });

        RelevantBill relevantBill = new RelevantBill();
        relevantBill.setCode("C-001");
        relevantBill.setAvailable(true);
        relevantBill.setDetails(details);
        this.relevantBillService.save(relevantBill);

        relevantBill.getDetails().remove(0);
        relevantBill.getDetails().get(0).setPrice(new BigDecimal("200.25"));
        RelevantBillDetail commodity = new RelevantBillDetail();
        commodity.setName("C001");
        commodity.setType("002");
        commodity.setPrice(new BigDecimal("868"));
        relevantBill.getDetails().add(commodity);
        this.relevantBillService.update(relevantBill);
    }
}
