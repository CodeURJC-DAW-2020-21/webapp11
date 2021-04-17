package es.urjc.code.daw.marketplace.api.sale.controller;

import es.urjc.code.daw.marketplace.api.common.RestResponseDto;
import es.urjc.code.daw.marketplace.api.sale.dto.*;
import es.urjc.code.daw.marketplace.api.sale.mapper.RestSaleMapper;
import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.service.AuthenticationService;
import es.urjc.code.daw.marketplace.service.ProductService;
import es.urjc.code.daw.marketplace.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SaleRestController {

    private static final String BASE_ROUTE = "/api/sales";

    private final SaleService saleService;
    private final ProductService productService;
    private final RestSaleMapper restSaleMapper;
    private final AuthenticationService authenticationService;

    public SaleRestController(SaleService saleService,
                              ProductService productService,
                              RestSaleMapper restSaleMapper,
                              AuthenticationService authenticationService) {
        this.saleService = saleService;
        this.productService = productService;
        this.restSaleMapper = restSaleMapper;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Find the specific details of the specified discount type if active")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the current active discount of the specified type",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "There is no known existence of the provided discount type",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester has no permissions to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The given discount type is currently inactive",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{type}",
            method = RequestMethod.GET
    )
    public ResponseEntity<?> findCurrentDiscount(@PathVariable("type") String saleType) {
        User loggedUser = authenticationService.getTokenUser();
        if(loggedUser == null) {
            final String message = "The token was invalid or no token was provided at all";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Ensure the user that requests to get the current one time discount is an admin and the sale type is not empty
        if(saleType == null || saleType.isEmpty()) {
            final String message = "You must provide a valid non-empty sale type to be found";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.BAD_REQUEST).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if(!loggedUser.isAdmin()) {
            final String message = "You have no permission to perform this operation (only the order owner or the admin)";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Return the details of the specified discount type if they exist
        if(saleType.equals("onetime")) {
            // Check if there is a one time discount active
            Optional<OneTimeDiscount> optional = saleService.getCurrentOtd();
            if (optional.isEmpty()) {
                final String message = "There is currently no one time discount active";
                RestResponseDto response = RestResponseDto.builder().status(HttpStatus.NOT_FOUND).content(message).build();
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            // Obtain the discount and map it to a DTO
            OneTimeDiscount discount = optional.get();
            Product product = productService.findProductById(discount.getProductId());
            FindOtdResponseDto content = restSaleMapper.asFindResponse(discount, product);
            content.setDiscountedPrice(((100 - content.getDiscount()) * product.getPrice()) / 100);
            // Return a successful response with the discount information
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(content).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else if(saleType.equals("accumulative")) {
            // Check if there is an accumulative discount active
            Optional<AccumulativeDiscount> optional = saleService.getCurrentAd();
            if (optional.isEmpty()) {
                final String message = "There is currently no accumulative discount active";
                RestResponseDto response = RestResponseDto.builder().status(HttpStatus.NOT_FOUND).content(message).build();
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            // Obtain the discount and map it to a DTO
            AccumulativeDiscount discount = optional.get();
            Product product = productService.findProductById(discount.getProductId());
            FindAdResponseDto content = restSaleMapper.asFindResponse(discount, product);
            content.setDiscountedPrice(((100 - content.getDiscount()) * product.getPrice()) / 100);
            // Return a successful response with the discount information
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(content).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            final String message = "No valid operation was specified in the request";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.BAD_REQUEST).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Updates the details of the discount of the given type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The details of the discount have been successfully updated",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester has no permissions to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The given discount type does not exist",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{type}",
            method = RequestMethod.PUT
    )
    public ResponseEntity<?> updateCurrentDiscount(@PathVariable("type") String saleType,
                                                   @RequestBody UpdateSaleRequestDto request) throws Exception {
        User loggedUser = authenticationService.getTokenUser();
        if(loggedUser == null) {
            final String message = "The token was invalid or no token was provided at all";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Ensure the user that requests to get the current one time discount is an admin and the sale type is not empty
        if(saleType == null || saleType.isEmpty()) {
            final String message = "You must provide a valid non-empty sale type to be updated";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.BAD_REQUEST).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if(!loggedUser.isAdmin()) {
            final String message = "You have no permission to perform this operation (only the order owner or the admin)";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Return the details of the specified discount type if they exist
        if(saleType.equals("onetime")) {
            // Map the request to the domain object
            OneTimeDiscount discount = restSaleMapper.asOtd(request);
            // Update the current one time discount (otd)
            OneTimeDiscount updated = saleService.updateCurrentOtd(discount);
            // Current discount disabled
            if(updated == null) {
                final String message = "The sale has been successfully disabled";
                RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(message).build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            // Return a successful response
            Product product = productService.findProductById(updated.getProductId());
            FindOtdResponseDto content = restSaleMapper.asFindResponse(updated, product);
            content.setDiscountedPrice(((100 - content.getDiscount()) * product.getPrice()) / 100);
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(content).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else if(saleType.equals("accumulative")) {
            // Map the request to the domain object
            AccumulativeDiscount discount = restSaleMapper.asAd(request);
            // Update the current accumulative discount (ad)
            AccumulativeDiscount updated = saleService.updateCurrentAd(discount);
            // Return a successful response
            if(updated == null) {
                final String message = "The sale has been successfully disabled";
                RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(message).build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            Product product = productService.findProductById(updated.getProductId());
            FindAdResponseDto content = restSaleMapper.asFindResponse(updated, product);
            content.setDiscountedPrice(((100 - content.getDiscount()) * product.getPrice()) / 100);
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(content).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            final String message = "No valid sale type was found in the request";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.BAD_REQUEST).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}