package es.urjc.code.daw.marketplace.web.order.controller;

import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.OrderService;
import es.urjc.code.daw.marketplace.service.SaleService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final SaleService saleService;
    private final ProductRepository productRepository;

    public OrderController(OrderService orderService,
                           UserService userService,
                           SaleService saleService,
                           ProductRepository productRepository) {
        this.orderService = orderService;
        this.userService = userService;
        this.saleService = saleService;
        this.productRepository = productRepository;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/services", method = RequestMethod.GET)
    public String listServices(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        model.addAttribute("isServices", true);
        model.addAttribute("isLoggedIn", "yes");
        model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        if(userPrincipal.getUser().isAdmin()) {
            model.addAttribute("isAdmin", "yes");
        }

        return "services";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping(path = "/service/{id}")
    public String displayService(@PathVariable("id") Long id,
                          @AuthenticationPrincipal UserPrincipal userPrincipal,
                          Model model) {

        model.addAttribute("isService", true);

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        Order order = orderService.findOrderById(id);
        if(!currentUser.isAdmin() && !order.getUser().equals(currentUser)) {
            throw new RuntimeException("You don't have access to this order");
        }

        model.addAttribute("isLoggedIn", "yes");
        model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        if(userPrincipal.getUser().isAdmin()) {
            model.addAttribute("isAdmin", "yes");
        }

        model.addAttribute("orderId", order.getId());
        model.addAttribute("orderCategory", order.getProduct().getCategory());
        model.addAttribute("orderPurchaseDate", order.getCreationDate().toString());
        model.addAttribute("orderExpiryDate", order.getExpiryDate().toString());
        if(order.isExpired()) {
            model.addAttribute("orderIsExpired", order.isExpired());
        }

        return "service";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/services/{page}/{amount}", method = RequestMethod.GET)
    public String findOrders(@PathVariable("page") Integer page,
                            @PathVariable("amount") Integer amount,
                            @AuthenticationPrincipal UserPrincipal userPrincipal,
                            Model model) {

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        List<Order> orders = orderService.findAllOrdersByUserId(currentUser.getId(), PageRequest.of(page - 1, amount));

        model.addAttribute("orders", orders);

        return "orders";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/order/{productId}/place", method = RequestMethod.GET)
    public String placeOrder(@PathVariable("productId") Long productId,
                             @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        Product product = productRepository.findById(productId).orElseThrow();

        Order order = Order.builder()
                .product(product)
                .finalCost(product.getPrice())
                .user(currentUser)
                .build();

        if(saleService.isEligibleForCurrentOtd(currentUser.getId(), productId)) {
            saleService.applyOtdDiscount(order);
        }

        if(saleService.isEligibleForCurrentAd(currentUser.getId(), productId)) {
            saleService.applyAdDiscount(order);
        }

        orderService.saveOrder(order);

        return "redirect:/services";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/order/{orderId}/renew", method = RequestMethod.GET)
    public String renewOrder(@PathVariable("orderId") Long orderId,
                             @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Order order = orderService.findOrderById(orderId);
        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        if(!currentUser.isAdmin() && !order.getUser().equals(currentUser)) {
            throw new RuntimeException("Access denied");
        }

        order.setFinalCost(order.getFinalCost() + order.getProduct().getPrice());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date().after(order.getExpiryDate()) ? new Date() : order.getExpiryDate());
        calendar.add(Calendar.MONTH, 1);
        order.setExpiryDate(calendar.getTime());

        orderService.saveOrder(order);

        return "redirect:/service/" + orderId;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/order/{orderId}/cancel", method = RequestMethod.GET)
    public String cancelOrder(@PathVariable("orderId") Long orderId,
                             @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Order order = orderService.findOrderById(orderId);
        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        if(!currentUser.isAdmin() && !order.getUser().equals(currentUser)) {
            throw new RuntimeException("Access denied");
        }

        order.setExpiryDate(new Date());

        orderService.saveOrder(order);

        return "redirect:/service/" + orderId;
    }

}