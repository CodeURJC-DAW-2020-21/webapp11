package es.urjc.code.daw.marketplace.api.order.controller;

import es.urjc.code.daw.marketplace.api.order.dto.CancelOrderResponseDto;
import es.urjc.code.daw.marketplace.api.order.dto.FindOrderResponseDto;
import es.urjc.code.daw.marketplace.api.order.dto.RenewOrderResponseDto;
import es.urjc.code.daw.marketplace.api.order.mapper.RestOrderMapper;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
import es.urjc.code.daw.marketplace.service.OrderService;
import es.urjc.code.daw.marketplace.service.PdfExporterService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {

    private static final String ROOT_ROUTE = "api/orders";

    private final OrderService orderService;
    private final RestOrderMapper restOrderMapper;
    private final PdfExporterService pdfExporterService;
    private final AuthenticationService authenticationService;

    public OrderRestController(OrderService orderService,
                               RestOrderMapper restOrderMapper,
                               PdfExporterService pdfExporterService,
                               AuthenticationService authenticationService) {
        this.orderService = orderService;
        this.restOrderMapper = restOrderMapper;
        this.pdfExporterService = pdfExporterService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(
            path = ROOT_ROUTE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<FindOrderResponseDto>> findServices(@RequestParam("page") Integer page,
                                                                   @RequestParam("amount") Integer amount) {
        User loggedUser = authenticationService.getTokenUser();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId(), PageRequest.of(page - 1, amount));
        List<FindOrderResponseDto> response = orders.stream().map(restOrderMapper::asFindResponse).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(
            path = ROOT_ROUTE + "/{id}",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindOrderResponseDto> findService(@PathVariable("id") Long serviceId) {
        User loggedUser = authenticationService.getTokenUser();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId());
        Order order = orderService.findOrderById((serviceId));
        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        if(!accessPermitted) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(restOrderMapper.asFindResponse(order));
    }

    @RequestMapping(
            path = ROOT_ROUTE + "/{id}/export",
            method = RequestMethod.GET
    )
    public void exportOrderToPdf(HttpServletResponse response, @PathVariable("id") Long orderId) throws Exception {
        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById(orderId);
        boolean accessPermitted = loggedUser.isAdmin() || order.getUser().equals(loggedUser);
        if(!accessPermitted) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "Attachment; filename=userOrder_" + order.getId() + "_" + order.getUser().getId() + ".pdf";
        response.setHeader(headerKey, headerValue);

        pdfExporterService.exportPdf(response, order);
    }

    @RequestMapping(
            path = ROOT_ROUTE + "/{id}/renew",
            method = RequestMethod.POST
    )
    public ResponseEntity<RenewOrderResponseDto> renewOrder(@PathVariable("id") Long orderId) {

        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById(orderId);
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId());

        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        if(!accessPermitted) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        order.setFinalCost(order.getFinalCost() + order.getProduct().getPrice());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date().after(order.getExpiryDate()) ? new Date() : order.getExpiryDate());
        calendar.add(Calendar.MONTH, 1);
        order.setExpiryDate(calendar.getTime());

        orderService.saveOrder(order);

        return ResponseEntity.ok(RenewOrderResponseDto.successful());
    }

    @RequestMapping(
            path = ROOT_ROUTE + "/{id}/cancel",
            method = RequestMethod.POST
    )
    public ResponseEntity<CancelOrderResponseDto> cancelOrder(@PathVariable("id") Long orderId) {

        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById(orderId);
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId());

        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        if(!accessPermitted) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        order.setExpiryDate(new Date());

        orderService.saveOrder(order);

        return ResponseEntity.ok(CancelOrderResponseDto.successful());
    }

}
