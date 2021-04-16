package es.urjc.code.daw.marketplace.web.error;

import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Represents the controller responsible for displaying
 * the error message associated to its route.
 */
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

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String handleError(@AuthenticationPrincipal UserPrincipal userPrincipal,
                              HttpServletRequest request, Model model) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        String url = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if(path.startsWith("/api") || (url != null && url.startsWith("/api"))) {
            return "redirect:/api/error";
        } else {
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

}