package es.urjc.code.daw.marketplace.api.order.controller;

import es.urjc.code.daw.marketplace.api.order.dto.FindOrderResponseDto;
import es.urjc.code.daw.marketplace.api.order.mapper.RestOrderMapper;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import es.urjc.code.daw.marketplace.service.OrderService;
import es.urjc.code.daw.marketplace.service.PdfExporterService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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






}
