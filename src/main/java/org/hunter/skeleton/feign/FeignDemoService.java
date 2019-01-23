package org.hunter.skeleton.feign;

import feign.RequestLine;
import org.hunter.skeleton.annotation.FeignClient;

import java.util.List;

/**
 * @author wujianchuan 2019/1/22
 */
@FeignClient(name = "feignDemoService")
public interface FeignDemoService {
    @RequestLine("GET /sys/menu/list")
    List<String> getMenu();
}