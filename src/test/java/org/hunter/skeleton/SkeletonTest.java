package org.hunter.skeleton;

import org.hunter.demo.model.RelevantBillDetail;
import org.hunter.demo.model.Order;
import org.hunter.demo.service.OrderService;
import org.hunter.Application;
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
public class SkeletonTest {
    @Autowired
    OrderService orderService;

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
}
