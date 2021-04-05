package es.urjc.code.daw.marketplace.api.product.controller;

import es.urjc.code.daw.marketplace.api.product.dto.FindProductResponseDto;
import es.urjc.code.daw.marketplace.api.product.dto.PlaceOrderResponseDto;
import es.urjc.code.daw.marketplace.api.product.mapper.RestProductMapper;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import es.urjc.code.daw.marketplace.service.*;
import es.urjc.code.daw.marketplace.util.EmailMessageFactory;
import org.apache.commons.lang3.StringUtils;
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
    private final UserService userService;
    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;
    private final OrderService orderService;

    public ProductRestController(ProductService productService,
                                 RestProductMapper restProductMapper,
                                 SaleService saleService,
                                 EmailService emailService,
                                 UserService userService,
                                 JwtTokenService tokenService,
                                 TokenExtractor tokenExtractor,
                                 OrderService orderService) {
        this.productService = productService;
        this.restProductMapper = restProductMapper;
        this.saleService = saleService;
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.tokenExtractor = tokenExtractor;
        this.orderService = orderService;
    }

    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<FindProductResponseDto>> findAllProducts() {

        List<Product> products = productService.findAllProducts();
        List<FindProductResponseDto> response = products.stream().map(restProductMapper::asFindResponse).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @RequestMapping(
            path = BASE_ROUTE + "/{id}/purchase",
            method = RequestMethod.POST
    )
    public ResponseEntity<PlaceOrderResponseDto> placeOrder(@PathVariable("id") Long productId) {

        User loggedUser = loggedUserFromToken();
        Product product = productService.findProductById(productId);

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
