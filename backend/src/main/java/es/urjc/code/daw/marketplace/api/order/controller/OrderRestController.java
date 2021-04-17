package es.urjc.code.daw.marketplace.api.order.controller;

import es.urjc.code.daw.marketplace.api.order.dto.*;
import es.urjc.code.daw.marketplace.api.order.mapper.RestOrderMapper;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.service.AuthenticationService;
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
import org.springframework.http.MediaType;
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

    @Operation(summary = "Finds the logged in user paginated orders")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns a list of orders according to the pagination properties",
                    content = {@Content(
                            schema = @Schema(implementation = FindOrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There were no orders to be found for the logged in user",
                    content = @Content
            ),

    })
    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<FindOrderResponseDto>> findAllOrders(@RequestParam("page") Integer page,
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

    @Operation(summary = "Finds the logged in user concrete order by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the concrete requested order",
                    content = {@Content(
                            array = @ArraySchema(schema = @Schema(implementation = FindOrderResponseDto.class))
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The service in question was not found",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}",
            method = RequestMethod.GET
    )
    public ResponseEntity<FindOrderResponseDto> findOrder(@PathVariable("id") Long orderId) {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        Order order = orderService.findOrderById(orderId);
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
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
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

    @Operation(summary = "Updates the details of a given order")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The order has been successfully updated",
                    content = {@Content(
                            schema = @Schema(implementation = PlaceOrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Some invalid details where provided that prevented the operation completion",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cannot update the order because it does not exist",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.POST
    )
    public ResponseEntity<PlaceOrderResponseDto> updateOrder(@RequestBody UpdateOrderRequestDto request) {
        // Obtain the currently logged in user (if not logged in an exception is thrown)
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the specified order id is present
        if(request.getOrderId() == null) return ResponseEntity.badRequest().build();
        Order order = orderService.findOrderById(request.getOrderId());
        // If there is no order with that id return not found
        if(order == null)  throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // Ensure that the order belongs to the logged user (or is an admin)
        boolean accessPermitted = loggedUser.isAdmin() || order.getUser().equals(loggedUser);
        if(!accessPermitted) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if(request.shouldCancel()) {
            // Set the expiration to now's date (expires the order immediately)
            order.setExpiryDate(TimeUtils.now());
            orderService.saveOrder(order);
        } else if(request.shouldRenew()) {
            // Add the price of the current renewal (total = accumulated_price + (product_monthly_price * months_renewed))
            order.setFinalCost(order.getFinalCost() + order.getProduct().getPrice() * request.getMonthsRenewed());
            // Change the expiry date to one month later
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date().after(order.getExpiryDate()) ? new Date() : order.getExpiryDate());
            calendar.add(Calendar.MONTH, request.getMonthsRenewed());
            order.setExpiryDate(calendar.getTime());
            orderService.saveOrder(order);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Returns a pdf of an order given its order id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The order from the user was found successfully",
                    content = {@Content(
                            schema = @Schema(implementation = PlaceOrderResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The service to be exported to pdf was not found",
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
        // The client is responsible to translate the transmitted pdf bytes into its mime type for the client download
    }

}
