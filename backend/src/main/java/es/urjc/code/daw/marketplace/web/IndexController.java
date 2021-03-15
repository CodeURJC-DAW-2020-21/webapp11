package es.urjc.code.daw.marketplace.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(path = { "", "/", "/index", "/home" })
    public String home(Model model) {
        model.addAttribute("isHome", true);
        return "home";
    }

    @GetMapping(path = "/about")
    public String about(Model model) {
        model.addAttribute("isAbout", true);
        return "about";
    }

    @GetMapping(path = "/legal")
    public String legal(Model model) {
        model.addAttribute("isLegal", true);
        return "legal";
    }

    @GetMapping(path = "/faq")
    public String faq(Model model) {
        model.addAttribute("isFaq", true);
        return "faq";
    }

    @GetMapping(path = "/register")
    public String register(Model model) {
        model.addAttribute("isRegister", true);
        return "register";
    }

    @GetMapping(path = "/screenshots")
    public String screenshots() {
        return "screenshots";
    }

    @GetMapping(path = "/profile")
    public String profile(Model model) {
        model.addAttribute("isProfile", true);
        return "profile";
    }


    @GetMapping(path = "/services")
    public String services(Model model) {
        model.addAttribute("isServices", true);
        return "services";
    }

}
