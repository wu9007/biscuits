package org.hv.demo.bundles.authority.controller;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;
import org.hv.biscuits.controller.Body;
import org.hv.biscuits.spine.model.User;
import org.hv.demo.bundles.authority.service.SecurityService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "home", auth = false)
public class GuestController extends AbstractController {
    private final SecurityService securityService;

    public GuestController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Action(actionId = "welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome to my home!";
    }

    @Action(actionId = "login", method = RequestMethod.POST)
    public String login(@RequestParam String avatar, @RequestParam String password) throws SQLException {
        return securityService.generateToken(avatar, password);
    }

    @Action(actionId = "register", method = RequestMethod.POST)
    public Body register(@RequestParam String avatar, @RequestParam String name, @RequestParam String password) throws SQLException, IllegalAccessException {
        if (avatar == null || password == null || name == null) {
            return Body.warning().title("失败").message("请正确填写信息");
        } else {
            User user = this.securityService.register(avatar, name, password);
            return Body.success().title("成功").message("恭喜！注册成功！").data(user);
        }
    }
}
