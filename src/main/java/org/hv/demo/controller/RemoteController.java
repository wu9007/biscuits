package org.hv.demo.controller;

import org.hv.biscuit.annotation.Action;
import org.hv.biscuit.annotation.Controller;
import org.hv.biscuit.controller.AbstractController;
import org.hv.biscuit.remote.DesEncryption;
import org.hv.biscuit.remote.Grab;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "remote", auth = false)
public class RemoteController extends AbstractController {

    @Action(actionId = "/bai_du_dom", method = RequestMethod.GET)
    public String baiDuDom() throws IOException {
        Grab<String> grab = Grab.newInstance("http://www.baidu.com");
        return grab.setConnectionRequestTimeout(100)
                .appendHeader("biscuit", "hello world!")
                .appendParams("name", "guy")
                .appendBody("alive", true)
                .setEncryption(DesEncryption.newInstance("sss"))
                .get(result -> result);
    }
}
