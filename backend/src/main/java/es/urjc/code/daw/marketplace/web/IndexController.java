package es.urjc.code.daw.marketplace.web;

import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class IndexController {

    @GetMapping(path = { "", "/", "/index", "/home" })
    public String home(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
        }

        model.addAttribute("isHome", true);
        return "home";
    }

    @GetMapping(path = "/about")
    public String about(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
        }

        model.addAttribute("isAbout", true);
        return "about";
    }

    @GetMapping(path = "/legal")
    public String legal(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
        }

        model.addAttribute("isLegal", true);
        return "legal";
    }

    @GetMapping(path = "/faq")
    public String faq(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
        }

        model.addAttribute("isFaq", true);
        return "faq";
    }

    @GetMapping(path = "/register")
    public String register(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
        }

        model.addAttribute("isRegister", true);
        return "register";
    }

    @GetMapping(path = "/screenshots")
    public String screenshots(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
        }

        return "screenshots";
    }


}
