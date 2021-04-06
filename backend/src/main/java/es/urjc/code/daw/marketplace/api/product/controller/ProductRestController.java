package es.urjc.code.daw.marketplace.api.product.controller;

import es.urjc.code.daw.marketplace.api.jwt.dto.GenerateTokenResponseDto;
import es.urjc.code.daw.marketplace.api.product.dto.FindProductResponseDto;
import es.urjc.code.daw.marketplace.api.product.dto.PlaceOrderResponseDto;
import es.urjc.code.daw.marketplace.api.product.mapper.RestProductMapper;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
import es.urjc.code.daw.marketplace.service.*;
import es.urjc.code.daw.marketplace.util.EmailMessageFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductRestController {

    private static final String BASE_ROUTE = "/api/products";

    private final ProductService productService;
    private final RestProductMapper restProductMapper;
    private final SaleService saleService;
    private final EmailService emailService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    public ProductRestController(ProductService productService,
                                 RestProductMapper restProductMapper,
                                 SaleService saleService,
                                 EmailService emailService,
                                 OrderService orderService,
                                 AuthenticationService authenticationService) {
        this.productService = productService;
        this.restProductMapper = restProductMapper;
        this.saleService = saleService;
        this.emailService = emailService;
        this.orderService = orderService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Finds a list of all products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of products was found succesfully",
                    content = {@Content(
                            array = @ArraySchema(schema = @Schema(implementation = FindProductResponseDto.class))
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Can not access to find products",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "There were no more products to be shown",
                    content = @Content
            ),

    })
    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<FindProductResponseDto>> findAllProducts() {
        List<Product> products = productService.findAllProducts();
        if(products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<FindProductResponseDto> response = products.stream().map(restProductMapper::asFindResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Finds an order with all the user´s products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The order from the user was found succesfully",
                    content = {@Content(
                            schema = @Schema(implementation = PlaceOrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot perform placing order",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cannot perform the order because the product does not exist",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}/purchase",
            method = RequestMethod.POST
    )
    public ResponseEntity<PlaceOrderResponseDto> placeOrder(@PathVariable("id") Long productId) {
        User loggedUser = authenticationService.getTokenUser();
        Product product = productService.findProductById(productId);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        Order order = Order.builder()
                .product(product)
                .finalCost(product.getPrice())
                .user(loggedUser)
                .build();

        saleService.applyOtdDiscount(order);
        saleService.applyAdDiscount(order);

        Order savedOrder = orderService.saveOrder(order);

        final String title = EmailMessageFactory.newPurchaseTitle(savedOrder);
        final String message = EmailMessageFactory.newPurchaseMessage(loggedUser, product);
        emailService.sendEmail(loggedUser.getEmail(), title, message);

        return ResponseEntity.ok(PlaceOrderResponseDto.successful());
    }

}
