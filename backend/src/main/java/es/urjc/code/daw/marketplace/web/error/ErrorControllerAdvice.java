package es.urjc.code.daw.marketplace.web.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {

    private final static String errorPath = "redirect:/error";

    @ExceptionHandler(value = Exception.class)
    public String handleError() {
        return errorPath;
    }

}
