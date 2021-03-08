package es.urjc.code.daw.marketplace.web.user.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleError(Model model, Exception exception) {
        System.out.println(exception.getMessage());
        return new ModelAndView("redirect:/error");
    }

}
