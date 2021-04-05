package es.urjc.code.daw.marketplace.api.sale.controller;

import es.urjc.code.daw.marketplace.api.sale.dto.*;
import es.urjc.code.daw.marketplace.api.sale.mapper.RestSaleMapper;
import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import es.urjc.code.daw.marketplace.service.ProductService;
import es.urjc.code.daw.marketplace.service.SaleService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class  SaleRestController {

    private static final String BASE_ROUTE = "/api/sales";

    private final SaleService saleService;
    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;
    private final UserService userService;
    private final ProductService productService;
    private final RestSaleMapper restSaleMapper;

    public SaleRestController(SaleService saleService,
                              JwtTokenService tokenService,
                              TokenExtractor tokenExtractor,
                              UserService userService,
                              ProductService productService,
                              RestSaleMapper restSaleMapper) {
        this.saleService = saleService;
        this.tokenService = tokenService;
        this.tokenExtractor = tokenExtractor;
        this.userService = userService;
        this.productService = productService;
        this.restSaleMapper = restSaleMapper;
    }

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

    @RequestMapping(
            path = BASE_ROUTE + "/otd",
            method = RequestMethod.PUT
    )
    public ResponseEntity<UpdateSaleResponseDto> updateOtdSale(@RequestBody UpdateOtdSaleRequestDto request) {

        User loggedUser = loggedUserFromToken();
        if(!loggedUser.isAdmin()) {
            throw new RuntimeException("Not authorized");
        }

        OneTimeDiscount discount = restSaleMapper.asOtd(request);
        saleService.updateCurrentOtd(
                discount.getStart(),
                discount.getStop(),
                discount.getDiscountPercentage(),
                discount.getProductId()
        );

        return ResponseEntity.ok(UpdateSaleResponseDto.successful());
    }

    private User loggedUserFromToken() {
        String token = tokenExtractor.containsToken() ? tokenExtractor.extractToken() : StringUtils.EMPTY;
        String email = tokenService.extractTokenSubject(token);
        User loggedUser = userService.findUserByEmail(email);
        if(loggedUser == null) {
            throw new RuntimeException("Access denied");
        } else {
            return loggedUser;
        }
    }

}