package es.urjc.code.daw.marketplace.web.error;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorControllerAdvice {

    private final static String errorPath = "redirect:/error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleError(Model model, Exception exception) {
        model.addAttribute("exception", exception.getMessage());
        return new ModelAndView(errorPath);
    }

}
