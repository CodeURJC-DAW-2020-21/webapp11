package es.urjc.code.daw.marketplace.api.order.controller;

import es.urjc.code.daw.marketplace.api.order.dto.*;
import es.urjc.code.daw.marketplace.api.order.mapper.RestOrderMapper;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
import es.urjc.code.daw.marketplace.service.*;
import es.urjc.code.daw.marketplace.util.EmailMessageFactory;
import es.urjc.code.daw.marketplace.util.TimeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private static final String BASE_ROUTE = "/api/orders";

    private final OrderService orderService;
    private final ProductService productService;
    private final SaleService saleService;
    private final EmailService emailService;
    private final RestOrderMapper restOrderMapper;
    private final PdfExporterService pdfExporterService;
    private final AuthenticationService authenticationService;

    public OrderRestController(OrderService orderService,
                               ProductService productService,
                               SaleService saleService,
                               EmailService emailService,
                               RestOrderMapper restOrderMapper,
                               PdfExporterService pdfExporterService,
                               AuthenticationService authenticationService) {
        this.orderService = orderService;
        this.productService = productService;
        this.saleService = saleService;
        this.emailService = emailService;
        this.restOrderMapper = restOrderMapper;
        this.pdfExporterService = pdfExporterService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Returns a certain amount of pages with a certain amount of services from the logged user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the generated pages with the services",
                    content = {@Content(
                            schema = @Schema(implementation = FindOrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There were no services to be found from the logged user",
                    content = @Content
            ),

    })
    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<FindOrderResponseDto>> findServices(@RequestParam("page") Integer page,
                                                                   @RequestParam("amount") Integer amount) {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId(), PageRequest.of(page - 1, amount));
        // If no orders are found we return the appropriate response code
        if (orders.isEmpty()) return ResponseEntity.notFound().build();
        // Find all the orders and map them to DTO's
        List<FindOrderResponseDto> response = orders.stream().map(restOrderMapper::asFindResponse).collect(Collectors.toList());
        // Send the response with all the orders (or services)
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns a service given a service id if the logged user has it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the solicited service",
                    content = {@Content(
                            array = @ArraySchema(schema = @Schema(implementation = FindOrderResponseDto.class))
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The service in question was not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The logged user does not have permission or does not have the service in question",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindOrderResponseDto> findService(@PathVariable("id") Long serviceId) {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById((serviceId));
        // Ensure that the order with the given id exists
        if (order == null) return ResponseEntity.notFound().build();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId());
        // Ensure that the order belongs to the logged user (or is an admin)
        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        // If the user is trying to access another user's order without authorization return unauthorized
        if(!accessPermitted) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // If everything went fine, we find the order and send the DTO response (with the order information)
        return ResponseEntity.ok(restOrderMapper.asFindResponse(order));
    }


    @Operation(summary = "Returns a pdf of an order given its order id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "The service to be exported to pdf was not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The logged used does not have permissions to generate a pdf from the orderId provided",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}/pdf",
            method = RequestMethod.GET
    )
    public void exportOrderToPdf(HttpServletResponse response, @PathVariable("id") Long orderId) throws Exception {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById(orderId);
        // If there is no order with that id return not found
        if(order == null)  throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // Ensure that the order belongs to the logged user (or is an admin)
        boolean accessPermitted = loggedUser.isAdmin() || order.getUser().equals(loggedUser);
        if(!accessPermitted) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        // Prepare the request specific properties or settings (in the future if want to be beautified move to a creation method)
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "Attachment; filename=userOrder_" + order.getId() + "_" + order.getUser().getId() + ".pdf";
        // Set the settings to the request
        response.setHeader(headerKey, headerValue);
        // Send the binary image to the client
        pdfExporterService.exportPdf(response, order);
    }

    @Operation(summary = "Returns if the renewal of a service has been successful")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The order has been renewed",
                    content = {@Content(
                            schema = @Schema(implementation = RenewOrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The service to be renewed was not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The logged user does not have permission or does not have the service in question to renew it",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}/renew",
            method = RequestMethod.POST
    )
    public ResponseEntity<RenewOrderResponseDto> renewOrder(@PathVariable("id") Long orderId) {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById(orderId);
        // If there is no order with that id return not found
        if (order == null) return ResponseEntity.notFound().build();
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId());
        // Ensure that the order belongs to the logged user (or is an admin)
        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        if(!accessPermitted) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        // Add the cost of one month the the current final cost
        order.setFinalCost(order.getFinalCost() + order.getProduct().getPrice());
        // Change the expiry date to one month later
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date().after(order.getExpiryDate()) ? new Date() : order.getExpiryDate());
        calendar.add(Calendar.MONTH, 1);
        order.setExpiryDate(calendar.getTime());
        // Save the order with the modified options
        orderService.saveOrder(order);
        // Return a successful response
        return ResponseEntity.ok(RenewOrderResponseDto.successful());
    }


    @Operation(summary = "Returns if the cancellation of a service has been successful")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The order has been canceled",
                    content = {@Content(
                            schema = @Schema(implementation = CancelOrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The service to be cancelled was not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The logged user does not have permission or does not have the service in question to cancel it",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}/cancel",
            method = RequestMethod.POST
    )
    public ResponseEntity<CancelOrderResponseDto> cancelOrder(@PathVariable("id") Long orderId) {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById(orderId);
        // If there is no order with that id return not found
        if (order == null) return ResponseEntity.notFound().build();
        // Ensure that the order belongs to the logged user (or is an admin)
        List<Order> orders = orderService.findAllOrdersByUserId(loggedUser.getId());
        boolean accessPermitted = loggedUser.isAdmin() || orders.contains(order);
        if(!accessPermitted) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        // Change the expiry date to now
        order.setExpiryDate(TimeUtils.now());
        // Save the order with the new changes
        orderService.saveOrder(order);
        // Return a successful response
        return ResponseEntity.ok(CancelOrderResponseDto.successful());
    }

    @Operation(summary = "Places an order to purchase the specified product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The order from the user was found successfully",
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
            path = BASE_ROUTE,
            method = RequestMethod.POST
    )
    public ResponseEntity<PlaceOrderResponseDto> placeOrder(@RequestBody PlaceOrderRequestDto request) {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        // Ensure that the requested product to be bought exists
        Product product = productService.findProductById(request.getProductId());
        if(product == null) return ResponseEntity.notFound().build();
        // Build the order
        Order order = Order.builder()
                .product(product)
                .finalCost(product.getPrice())
                .user(loggedUser)
                .build();
        // Apply discounts if available
        saleService.applyOtdDiscount(order);
        saleService.applyAdDiscount(order);
        // Save the order
        Order savedOrder = orderService.saveOrder(order);
        // Send an email with the service receipt
        final String title = EmailMessageFactory.newPurchaseTitle(savedOrder);
        final String message = EmailMessageFactory.newPurchaseMessage(loggedUser, product);
        emailService.sendEmail(loggedUser.getEmail(), title, message);
        // Return a successful response
        return ResponseEntity.ok(PlaceOrderResponseDto.successful());
    }

}
