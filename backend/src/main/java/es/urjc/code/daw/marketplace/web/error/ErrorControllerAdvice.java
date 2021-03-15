package es.urjc.code.daw.marketplace.web.error;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorControllerAdvice {

    private final static String errorPath = "redirect:/error404";

    @ExceptionHandler(value = Exception.class)
    public String handleError() {
        return errorPath;
    }

}
