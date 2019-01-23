package org.hunter.skeleton;

import org.hunter.skeleton.feign.FeignDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wujianchuan 2019/1/23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {
    @Autowired
    FeignDemoService feignTestService;

    @Test
    public void test() {
        feignTestService.getMenu().forEach(System.out::println);
    }
}
