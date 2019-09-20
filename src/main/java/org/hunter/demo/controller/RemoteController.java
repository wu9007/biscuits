package org.hunter.demo.controller;

import org.hunter.skeleton.annotation.Action;
import org.hunter.skeleton.annotation.Controller;
import org.hunter.skeleton.controller.AbstractController;
import org.hunter.skeleton.remot.DesEncryption;
import org.hunter.skeleton.remot.Grab;
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
