package org.hv.demo.controller;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;
import org.hv.biscuits.controller.Body;
import org.hv.biscuits.controller.UserView;
import org.hv.biscuits.spine.model.User;
import org.hv.biscuits.utils.TokenUtil;
import org.hv.demo.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "guest", auth = false)
public class GuestController extends AbstractController {
    private final UserService userService;
    private final TokenUtil tokenUtil;

    public GuestController(UserService userService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    @Action(actionId = "login", method = RequestMethod.POST)
    @ResponseBody
    public Body login(@RequestBody Map<String, String> encodeAppUserInfo) throws SQLException {
        UserView userView = UserView.newInstance()
                .setAvatar("ADMIN")
                .setName("管理员")
                .setAuthIds(new ArrayList())
                .setBundleIds(new HashSet());
        String token = tokenUtil.generateToken(userView);
        return Body.success()
                .title("登录成功")
                .message(String.format("%s 成功登录系统。", "管理员"))
                .data(userView)
                .token(token);
    }

    @Action(actionId = "register", method = RequestMethod.POST)
    public Body register(@RequestParam String avatar, @RequestParam String name, @RequestParam String password) throws SQLException, IllegalAccessException {
        if (avatar == null || password == null || name == null) {
            return Body.warning().message("Please fill in the information correctly.");
        } else {
            User user = this.userService.register(avatar, name, password);
            return Body.success().message("Congratulations! Registration succeeded!").data(user);
        }
    }
}
