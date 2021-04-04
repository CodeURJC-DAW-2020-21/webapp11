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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(
            path = ROOT_ROUTE,
            method = RequestMethod.GET
    )
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

    @RequestMapping(
            path = ROOT_ROUTE + "/{id}",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindOrderResponseDto> findService(@PathVariable("id") Long serviceId) {
        User loggedUser  = loggedUserFromToken();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId());
        Order order = orderService.findOrderById((serviceId));
        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        if(!accessPermitted) {
            throw new RuntimeException("Access denied, you do not have permission");
        }
        return ResponseEntity.ok(restOrderMapper.asFindResponse(order));
    }

    @RequestMapping(
            path = ROOT_ROUTE + "/{id}/export",
            method = RequestMethod.GET
    )
    public void exportOrderToPdf(HttpServletResponse response, @PathVariable("id") Long orderId) throws Exception {
        User loggedUser = loggedUserFromToken();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUserFromToken().getId());
        Order order = orderService.findOrderById(orderId);
        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        if(!accessPermitted) {
            throw new RuntimeException("Access denied, you do not have permission");
        }

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "Attachment; filename=userOrder_" + order.getId() + "_" + order.getUser().getId() + ".pdf";
        response.setHeader(headerKey, headerValue);

        pdfExporterService.exportPdf(response, order);

    }


}
