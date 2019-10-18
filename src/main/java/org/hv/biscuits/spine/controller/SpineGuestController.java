package org.hv.biscuits.spine.controller;

import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;
import org.hv.biscuits.controller.Body;
import org.hv.biscuits.spine.model.User;
import org.hv.biscuits.spine.service.SpineSecurityService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
@Controller(bundleId = "guest", auth = false)
public class SpineGuestController extends AbstractController {
    private final SpineSecurityService spineSecurityService;

    public SpineGuestController(SpineSecurityService spineSecurityService) {
        this.spineSecurityService = spineSecurityService;
    }

    @Action(actionId = "login", method = RequestMethod.POST)
    public String login(@RequestParam String avatar, @RequestParam String password) throws SQLException {
        return spineSecurityService.generateToken(avatar, password);
    }

    @Action(actionId = "register", method = RequestMethod.POST)
    public Body<User> register(@RequestParam String avatar, @RequestParam String name, @RequestParam String password) throws SQLException, IllegalAccessException {
        if (avatar == null || password == null || name == null) {
            return Body.newWaringInstance("Fail", "Please fill in the information correctly.", null);
        } else {
            User user = this.spineSecurityService.register(avatar, name, password);
            return Body.newSuccessInstance("Success", "Congratulations! Registration succeeded!", user);
        }
    }
}
