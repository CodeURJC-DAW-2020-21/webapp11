package es.urjc.code.daw.marketplace.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(path = { "", "/", "/index", "/home" })
    public String index(Model model) {
        model.addAttribute("isHome", true);
        return "home";
    }

}
