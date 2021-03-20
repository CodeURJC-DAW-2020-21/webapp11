package es.urjc.code.daw.marketplace.web.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public String handleError() {
        return "redirect:/error";
    }

}
