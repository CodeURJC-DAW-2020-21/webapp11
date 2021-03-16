package es.urjc.code.daw.marketplace.web.error;

import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
public class CustomErrorController implements ErrorController {

    private final UserService userService;

    public CustomErrorController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        }

        return "error";
    }

}