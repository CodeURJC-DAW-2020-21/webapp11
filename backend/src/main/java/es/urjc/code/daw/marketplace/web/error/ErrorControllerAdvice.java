package es.urjc.code.daw.marketplace.web.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public String handleError(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if(path.startsWith("/api")) {
            return "redirect:/api/error";
        } else {
            return "redirect:/error";
        }
    }

}
