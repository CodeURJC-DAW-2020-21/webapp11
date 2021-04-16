package es.urjc.code.daw.marketplace.api;

import es.urjc.code.daw.marketplace.api.jwt.controller.TokenRestController;
import es.urjc.code.daw.marketplace.api.order.controller.OrderRestController;
import es.urjc.code.daw.marketplace.api.product.controller.ProductRestController;
import es.urjc.code.daw.marketplace.api.sale.controller.SaleRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorRestController {

    @Operation(summary = "Send an error if some process has failed o the page does not exist")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "There was an error when trying to perform a operation",
                    content = @Content
            ),
    })
    @RequestMapping(path = "/api/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
