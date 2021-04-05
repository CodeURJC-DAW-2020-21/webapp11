package es.urjc.code.daw.marketplace.api.sale.controller;

import es.urjc.code.daw.marketplace.api.sale.dto.*;
import es.urjc.code.daw.marketplace.api.sale.mapper.RestSaleMapper;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.service.ProductService;
import es.urjc.code.daw.marketplace.service.SaleService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class  SaleRestController {

    private static final String BASE_ROUTE = "/api/sales";

    private final SaleService saleService;
    private final UserService userService;
    private final ProductService productService;
    private final RestSaleMapper restSaleMapper;

    public SaleRestController(SaleService saleService,
                              UserService userService,
                              ProductService productService,
                              RestSaleMapper restSaleMapper) {
        this.saleService = saleService;
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
}