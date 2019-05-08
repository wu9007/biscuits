package org.hunter.skeleton;

import org.hunter.demo.model.Commodity;
import org.hunter.demo.model.Order;
import org.hunter.demo.service.OrderService;
import org.hunter.Application;
import org.hunter.pocket.model.BaseEntity;
import org.junit.After;
import org.junit.Before;
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
import java.util.Date;
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

    private List<Commodity> commodities = new ArrayList<>();

    @Before
    public void setup() {
        IntStream.range(1, 20).forEach((index) -> {
            Commodity commodity = new Commodity();
            commodity.setUuid(String.valueOf(index));
            commodity.setName("c1");
            commodity.setType("001");
            commodity.setPrice(new BigDecimal("11.2"));
            commodities.add(commodity);
        });
    }

    @After
    public void destroy() {
    }

    @Test
    public void test1() throws SQLException, IllegalAccessException {
        Order order = new Order();
        order.setCode("C-001");
        order.setState(null);
        order.setType("001");
        order.setTime(LocalDateTime.now());
        order.setPrice(new BigDecimal("99.56789"));
        order.setDay(LocalDate.now());
        order.setCommodities(commodities);
        this.orderService.save(order);

        order.getCommodities().remove(0);
        order.getCommodities().get(0).setPrice(new BigDecimal("200.25"));
        Commodity commodity = new Commodity();
        commodity.setName("C001");
        commodity.setType("002");
        commodity.setPrice(new BigDecimal("868"));
        order.getCommodities().add(commodity);
        this.orderService.update(order);
    }

    @Test
    public void test2() {
        Map<String, Commodity> result = commodities.stream().collect(Collectors.toMap(BaseEntity::getUuid, commodity -> commodity, (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, LinkedHashMap::new));
        System.out.println(result.size());
    }
}
