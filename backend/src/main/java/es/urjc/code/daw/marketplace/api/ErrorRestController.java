package es.urjc.code.daw.marketplace.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@Controller
@ControllerAdvice
public class ErrorRestController {

    @Operation(summary = "Send an error if some process has failed")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "There was an error when trying to perform a operation",
                    content = @Content
            ),
    })
    @RequestMapping(path = "/api/error", method = RequestMethod.GET)
    public ResponseEntity<String> error() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Send an error if the page does not exist")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "There was an error when trying to perform a operation",
                    content = @Content
            ),
    })
    @RequestMapping(path = "/api/not_found", method = RequestMethod.GET)
    public ResponseEntity<String> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleError(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if(path.startsWith("/api")) {
            return "redirect:/api/not_found";
        } else {
            return "redirect:/error";
        }
    }

}