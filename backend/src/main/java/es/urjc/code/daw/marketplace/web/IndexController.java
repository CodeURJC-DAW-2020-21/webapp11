package es.urjc.code.daw.marketplace.web;

import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = { "", "/", "/index", "/home" })
    public String home(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        }

        model.addAttribute("isHome", true);
        return "home";
    }

    @GetMapping(path = "/about")
    public String about(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        }

        model.addAttribute("isAbout", true);
        return "about";
    }

    @GetMapping(path = "/legal")
    public String legal(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        }

        model.addAttribute("isLegal", true);
        return "legal";
    }

    @GetMapping(path = "/faq")
    public String faq(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        }

        model.addAttribute("isFaq", true);
        return "faq";
    }

    @GetMapping(path = "/register")
    public String register(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        }

        model.addAttribute("isRegister", true);
        return "register";
    }

    @GetMapping(path = "/screenshots")
    public String screenshots(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        }

        return "screenshots";
    }

}
