package org.hv.demo.controller;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;
import org.hv.biscuits.controller.Body;
import org.hv.biscuits.spine.model.User;
import org.hv.demo.service.SecurityService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "home", auth = false)
public class HomeController extends AbstractController {
    private final SecurityService securityService;

    public HomeController(SecurityService securityService) {
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
    public Body<User> register(@RequestParam String avatar, @RequestParam String name, @RequestParam String password) throws SQLException, IllegalAccessException {
        if (avatar == null || password == null || name == null) {
            return Body.newWaringInstance("失败", "请正确填写信息", null);
        } else {
            User user = this.securityService.register(avatar, name, password);
            return Body.newSuccessInstance("成功", "恭喜！注册成功！", user);
        }
    }
}
