package es.urjc.code.daw.marketplace.api.sale.controller;

import es.urjc.code.daw.marketplace.api.sale.dto.*;
import es.urjc.code.daw.marketplace.api.sale.mapper.RestSaleMapper;
import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
import es.urjc.code.daw.marketplace.service.ProductService;
import es.urjc.code.daw.marketplace.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Checks the application of a one time discount for a given product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtains the current one time discount",
                    content = {
                            @Content(schema = @Schema(implementation = FindOtdResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to apply the one time discount",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is currently no one time discount active",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/onetime",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindOtdResponseDto> findCurrentOneTimeDiscount() {
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the user that requests to get the current one time discount is an admin
        if(!loggedUser.isAdmin()) return ResponseEntity.badRequest().build();
        // Check if there is a one time discount active
        Optional<OneTimeDiscount> optional = saleService.getCurrentOtd();
        if (optional.isEmpty()) return ResponseEntity.notFound().build();
        // Obtain the discount and map it to a DTO
        OneTimeDiscount discount = optional.get();
        Product product = productService.findProductById(discount.getProductId());
        FindOtdResponseDto response = restSaleMapper.asFindResponse(discount, product);
        // Return a successful response with the discount information
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Checks the application of a cumulative discount for a given product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtains the cumulative one time discount",
                    content = {
                            @Content(schema = @Schema(implementation = FindAdResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to apply the cumulative discount",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is currently no cumulative discount active",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/accumulative",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindAdResponseDto> findCurrentAccumulativeDiscount() {
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the user that requests to get the current one time discount is an admin
        if(!loggedUser.isAdmin()) return ResponseEntity.badRequest().build();
        // Check if there is an accumulative discount active
        Optional<AccumulativeDiscount> optional = saleService.getCurrentAd();
        if(optional.isEmpty()) return ResponseEntity.notFound().build();
        // Obtain the discount and map it to a DTO
        AccumulativeDiscount discount = optional.get();
        Product product = productService.findProductById(discount.getProductId());
        FindAdResponseDto response = restSaleMapper.asFindResponse(discount, product);
        // Return a successful response with the discount information
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Updates the one time discount for a given user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updates the current one time discount",
                    content = {
                            @Content(schema = @Schema(implementation = UpdateSaleResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "It's not possible to update the one time discount",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/onetime",
            method = RequestMethod.PUT
    )
    public ResponseEntity<UpdateSaleResponseDto> updateCurrentOneTimeDiscount(@RequestBody UpdateOtdSaleRequestDto request) {
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the user that requests to update the current one time discount is an admin
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // Map the request to the domain object
        OneTimeDiscount discount = restSaleMapper.asOtd(request);
        // Update the current one time discount (otd)
        saleService.updateCurrentOtd(discount);
        // Return a successful response
        return ResponseEntity.ok(UpdateSaleResponseDto.successful());
    }

    @Operation(summary = "Updates the cumulative discount for a given user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updates the current cumulative discount",
                    content = {
                            @Content(schema = @Schema(implementation = UpdateSaleResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "It's not possible to update the cumulative discount",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/accumulative",
            method = RequestMethod.PUT
    )
    public ResponseEntity<UpdateSaleResponseDto> updateCurrentAccumulativeDiscount(@RequestBody UpdateAdSaleRequestDto request) {
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the user that requests to update the current one time discount is an admin
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // Map the request to the domain object
        AccumulativeDiscount discount = restSaleMapper.asAd(request);
        // Update the current accumulative discount (ad)
        saleService.updateCurrentAd(discount);
        // Return a successful response
        return ResponseEntity.ok(UpdateSaleResponseDto.successful());
    }

}