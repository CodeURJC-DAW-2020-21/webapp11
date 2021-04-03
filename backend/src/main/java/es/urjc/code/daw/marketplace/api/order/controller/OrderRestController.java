package es.urjc.code.daw.marketplace.api.order.controller;

import es.urjc.code.daw.marketplace.api.order.dto.FindOrderResponseDto;
import es.urjc.code.daw.marketplace.api.order.mapper.RestOrderMapper;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import es.urjc.code.daw.marketplace.service.OrderService;
import es.urjc.code.daw.marketplace.service.PdfExporterService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {

    private static final String ROOT_ROUTE = "api/orders";
    private final UserService userService;
    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;
    private final OrderService orderService;
    private final RestOrderMapper restOrderMapper;
    private final PdfExporterService pdfExporterService;


    public OrderRestController(UserService userService,
                               JwtTokenService tokenService,
                               TokenExtractor tokenExtractor,
                               OrderService orderService,
                               RestOrderMapper restOrderMapper,
                               PdfExporterService pdfExporterService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.tokenExtractor = tokenExtractor;
        this.orderService = orderService;
        this.restOrderMapper = restOrderMapper;
        this.pdfExporterService = pdfExporterService;
    }


    public ResponseEntity<List<FindOrderResponseDto>> findServices(@RequestParam("page") Integer page,
                                                                   @RequestParam("amount") Integer amount) {

        User loggedUser = loggedUserFromToken();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId(), PageRequest.of(page-1, amount));
        List<FindOrderResponseDto> response = orders.stream().map(restOrderMapper::asFindResponse).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private User loggedUserFromToken() {
        String token = tokenExtractor.containsToken() ? tokenExtractor.extractToken() : StringUtils.EMPTY;
        String email = tokenService.extractTokenSubject(token);
        User loggedUser = userService.findUserByEmail(email);
        if (loggedUser == null){
            throw new RuntimeException("Access denied");
        } else {
            return loggedUser;
        }

    }


}
