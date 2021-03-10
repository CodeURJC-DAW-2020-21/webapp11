package es.urjc.code.daw.marketplace.web.product.controller;

import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(path = "/pricing", method = RequestMethod.GET)
    public String productsPricing(Model model) {

        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        final String viewIndicator = "isPricing";
        model.addAttribute(viewIndicator, "yes");

        return "pricing";
    }

}
