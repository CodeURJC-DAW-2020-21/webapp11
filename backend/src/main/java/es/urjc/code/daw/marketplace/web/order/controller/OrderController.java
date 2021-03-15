package es.urjc.code.daw.marketplace.web.order.controller;

import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.OrderService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService,
                           UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping(path = "/service/{id}")
    public String service(@PathVariable("id") Long id,
                          @AuthenticationPrincipal UserPrincipal principal,
                          Model model) {

        model.addAttribute("isService", true);

        User currentUser = userService.findUserByEmail(principal.getUsername());
        Order order = orderService.findOrderById(id);
        if(!currentUser.isAdmin() && !order.getUser().equals(currentUser)) {
            throw new RuntimeException("You don't have access to this order");
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

}