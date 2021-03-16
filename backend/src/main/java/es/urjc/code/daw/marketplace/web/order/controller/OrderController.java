package es.urjc.code.daw.marketplace.web.order.controller;

import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.OrderService;
import es.urjc.code.daw.marketplace.service.PdfExporterService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final PdfExporterService pdfExport;

    public OrderController(OrderService orderService,
                           UserService userService, PdfExporterService pdfExport) {
        this.orderService = orderService;
        this.userService = userService;

        this.pdfExport = pdfExport;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/services", method = RequestMethod.GET)
    public String listServices(@AuthenticationPrincipal UserPrincipal principal, Model model) {

        model.addAttribute("isServices", true);

        User currentUser = userService.findUserByEmail(principal.getUsername());
        List<Order> orders = orderService.findAllOrdersByUserId(currentUser.getId());
        model.addAttribute("orders", orders);

        return "services";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping(path = "/service/{id}")
    public String displayService(@PathVariable("id") Long id,
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
    
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/order/{orderId}/export_pdf")
    public void exportToPDF(HttpServletResponse response, @PathVariable("orderId") Long orderId, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        Order currentOrder = orderService.findOrderById(orderId);
        if (!currentUser.isAdmin() && !currentOrder.getUser().equals(currentUser)){
            throw new RuntimeException("Access Denied");
        }

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=userorder_" + currentOrder.getId() + "_" + currentOrder.getUser().getId() + ".pdf";
        response.setHeader(headerKey, headerValue);

        pdfExport.exportPdf(response, currentOrder);

    }

}