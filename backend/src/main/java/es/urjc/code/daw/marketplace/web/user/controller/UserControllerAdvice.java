package es.urjc.code.daw.marketplace.web.user.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public String handleRegister(Model model) {
        model.addAttribute("error", "An error occurred!");
        return "register";
    }

}
