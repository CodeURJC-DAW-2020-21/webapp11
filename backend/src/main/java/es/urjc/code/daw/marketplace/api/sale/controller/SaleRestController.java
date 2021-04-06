package es.urjc.code.daw.marketplace.api.sale.controller;

import es.urjc.code.daw.marketplace.api.jwt.dto.GenerateTokenResponseDto;
import es.urjc.code.daw.marketplace.api.sale.dto.DisableSaleResponseDto;
import es.urjc.code.daw.marketplace.api.sale.dto.UpdateAdSaleRequestDto;
import es.urjc.code.daw.marketplace.api.sale.dto.UpdateSaleResponseDto;
import es.urjc.code.daw.marketplace.api.sale.mapper.RestSaleMapper;
import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.api.sale.dto.*;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import es.urjc.code.daw.marketplace.service.ProductService;
import es.urjc.code.daw.marketplace.service.SaleService;
import es.urjc.code.daw.marketplace.service.UserService;
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
                    content = {@Content(
                            schema = @Schema(implementation = GenerateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to apply the one time discount",
                    content = @Content
            ),
    })

    @RequestMapping(
            path = BASE_ROUTE + "/otd",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindOtdResponseDto> findOneTimeDiscount() {
        Optional<OneTimeDiscount> optional = saleService.getCurrentOtd();
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        OneTimeDiscount otd = optional.get();
        Product product = productService.findProductById(otd.getProductId());
        FindOtdResponseDto response = restSaleMapper.asFindResponse(otd, product);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Checks the application of a cumulative discount for a given product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtains the cumulative one time discount",
                    content = {@Content(
                            schema = @Schema(implementation = GenerateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to apply the cumulative discount",
                    content = @Content
            ),
    })

    @RequestMapping(
            path = BASE_ROUTE + "/ad",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindAdResponseDto> findAccumulativeDiscount() {
        Optional<AccumulativeDiscount> optional = saleService.getCurrentAd();
        if(optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AccumulativeDiscount ad = optional.get();
        Product product = productService.findProductById(ad.getProductId());
        FindAdResponseDto response = restSaleMapper.asFindResponse(ad, product);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Updates the one time discount for a given user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updates the current one time discount",
                    content = {@Content(
                            schema = @Schema(implementation = GenerateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to update the one time discount",
                    content = @Content
            ),
    })

    @RequestMapping(
            path = BASE_ROUTE + "/otd",
            method = RequestMethod.PUT
    )
    public ResponseEntity<UpdateSaleResponseDto> updateOtdSale(@RequestBody UpdateOtdSaleRequestDto request) {

        User loggedUser = authenticationService.getTokenUser();
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        OneTimeDiscount discount = restSaleMapper.asOtd(request);
        saleService.updateCurrentOtd(
                discount.getStart(),
                discount.getStop(),
                discount.getDiscountPercentage(),
                discount.getProductId()
        );

        return ResponseEntity.ok(UpdateSaleResponseDto.successful());
    }

    @Operation(summary = "Updates the cumulative discount for a given user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updates the current cumulative discount",
                    content = {@Content(
                            schema = @Schema(implementation = GenerateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to update the cumulative discount",
                    content = @Content
            ),
    })

    @RequestMapping(
            path = BASE_ROUTE + "/ad",
            method = RequestMethod.PUT
    )
    public ResponseEntity<UpdateSaleResponseDto> updateAdSale(@RequestBody UpdateAdSaleRequestDto request) {

        User loggedUser = authenticationService.getTokenUser();
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        AccumulativeDiscount discount = restSaleMapper.asAd(request);
        saleService.updateCurrentAd(
                discount.getStart(),
                discount.getStop(),
                discount.getDiscountPercentage(),
                discount.getProductId(),
                discount.getBulkAmount()
        );

        return ResponseEntity.ok(UpdateSaleResponseDto.successful());
    }

    @Operation(summary = "Disable the one time discount for a given user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Disable the current one time discount",
                    content = {@Content(
                            schema = @Schema(implementation = GenerateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to disable the one time discount",
                    content = @Content
            ),
    })

    @RequestMapping(
            path = BASE_ROUTE + "/otd/disable",
            method = RequestMethod.POST
    )
    public ResponseEntity<DisableSaleResponseDto> disableOtdSale() {
        User loggedUser = authenticationService.getTokenUser();
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        saleService.disableCurrentOtd();

        return ResponseEntity.ok(DisableSaleResponseDto.successful());
    }

    @Operation(summary = "Disable the cumulative discount for a given user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Disables the current cumulative discount",
                    content = {@Content(
                            schema = @Schema(implementation = GenerateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "It's not possible to disable the cumulative discount",
                    content = @Content
            ),
    })

    @RequestMapping(
            path = BASE_ROUTE + "/ad/disable",
            method = RequestMethod.POST
    )
    public ResponseEntity<DisableSaleResponseDto> disableAdSale() {
        User loggedUser = authenticationService.getTokenUser();
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        saleService.disableCurrentAd();

        return ResponseEntity.ok(DisableSaleResponseDto.successful());
    }

}