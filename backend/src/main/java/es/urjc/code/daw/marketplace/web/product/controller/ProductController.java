package es.urjc.code.daw.marketplace.web.product.controller;

import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(path = "/pricing", method = RequestMethod.GET)
    public String productsPricing(@RequestParam(value = "selected", required = false) String categoryToLoad, Model model) {

        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        final String viewIndicator = "isPricing";
        model.addAttribute(viewIndicator, "yes");

        if(StringUtils.isNotEmpty(categoryToLoad) && StringUtils.isNotBlank(categoryToLoad)) {
            model.addAttribute("loadCategory", categoryToLoad);
        }

        return "pricing";
    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public String fetchStatistics(Model model) {

        Set<Map.Entry<String,Integer>> weeklyCategoryPurchases = productService.findCategoryToWeeklyPurchases().entrySet();
        model.addAttribute("weeklyCategoryPurchases", weeklyCategoryPurchases);

        List<Integer> salesPerDayInWeek = productService.findSalesPerDayInWeek();
        model.addAttribute("salesPerDayInWeek", salesPerDayInWeek);

        Long accumulatedCapital = productService.findAccumulatedCapital();
        model.addAttribute("accumulatedCapital", accumulatedCapital);

        return "";
    }

}
