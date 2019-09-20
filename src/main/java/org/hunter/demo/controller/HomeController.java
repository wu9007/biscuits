package org.hunter.demo.controller;

import org.hunter.demo.service.SecurityService;
import org.hunter.skeleton.annotation.Action;
import org.hunter.skeleton.annotation.Controller;
import org.hunter.skeleton.controller.AbstractController;
import org.hunter.skeleton.controller.Body;
import org.hunter.skeleton.spine.model.User;
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

    @Action(actionId = "/welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome to my home!";
    }

    @Action(actionId = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String avatar, @RequestParam String password) throws SQLException {
        return securityService.generateToken(avatar, password);
    }

    @Action(actionId = "/register", method = RequestMethod.POST)
    public Body<User> register(@RequestParam String avatar, @RequestParam String name, @RequestParam String password) throws SQLException, IllegalAccessException {
        if (avatar == null || password == null || name == null) {
            return Body.newWaringInstance("失败", "请正确填写信息", null);
        } else {
            User user = this.securityService.register(avatar, name, password);
            return Body.newSuccessInstance("成功", "恭喜！注册成功！", user);
        }
    }
}
