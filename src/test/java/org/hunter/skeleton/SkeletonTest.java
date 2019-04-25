package org.hunter.skeleton;

import org.hunter.demo.service.OrderService;
import org.hunter.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wujianchuan 2019/1/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SkeletonTest {
    @Autowired
    OrderService orderService;


    @Before
    public void setup() {
    }

    @After
    public void destroy() {
    }

    @Test
    public void test1() {
        this.orderService.updateType(null);
    }
}
