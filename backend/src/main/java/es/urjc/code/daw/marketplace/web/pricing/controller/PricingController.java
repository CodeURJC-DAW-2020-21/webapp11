package es.urjc.code.daw.marketplace.web.pricing.controller;

import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PricingController {

    private final ProductRepository productRepository;

    public PricingController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(path = "/pricing", method = RequestMethod.GET)
    public String loadPricing(Model model) {
        model.addAttribute("isPricing", true);

        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "pricing";
    }


    /*

    Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        products.forEach(System.out::println);
     */

}
