package org.hv.demo.bundles.authority.controller;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;
import org.hv.biscuits.remote.DesEncryption;
import org.hv.biscuits.remote.Grab;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "remote", auth = false)
public class RemoteController extends AbstractController {

    @Action(actionId = "bai_du_dom", method = RequestMethod.GET)
    public String baiDuDom() throws IOException {
        Grab<String> grab = Grab.newInstance("http://www.baidu.com");
        return grab.setConnectionRequestTimeout(100)
                .appendHeader("biscuits", "hello world!")
                .appendParams("name", "guy")
                .appendBody("alive", true)
                .setEncryption(DesEncryption.newInstance("sss"))
                .get(result -> result);
    }
}
