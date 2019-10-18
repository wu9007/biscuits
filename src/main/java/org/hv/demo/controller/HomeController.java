package org.hv.demo.controller;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "home", auth = false)
public class HomeController extends AbstractController {

    @Action(actionId = "welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome to my home!";
    }
}
