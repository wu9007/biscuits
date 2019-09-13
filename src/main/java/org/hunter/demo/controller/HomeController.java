package org.hunter.demo.controller;

import org.hunter.demo.service.SecurityService;
import org.hunter.skeleton.annotation.Action;
import org.hunter.skeleton.annotation.Controller;
import org.hunter.skeleton.controller.AbstractController;
import org.hunter.skeleton.controller.Body;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "hunter", auth = false)
public class HomeController extends AbstractController {
    private final SecurityService securityService;

    public HomeController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Action(actionId = "/welcome", method = RequestMethod.GET)
    public Body<String> welcome() {
        return Body.newSuccessInstance("welcome to hunter village");
    }

    @Action(actionId = "/login", method = RequestMethod.POST)
    public Body<String> login(@RequestParam String avatar, @RequestParam String password) throws SQLException {
        String token = securityService.generateToken(avatar, password);
        return Body.newSuccessInstance(token);
    }
}
