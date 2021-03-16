package es.urjc.code.daw.marketplace.web.sale.controller;

import es.urjc.code.daw.marketplace.service.ProductService;
import es.urjc.code.daw.marketplace.service.SaleService;
import es.urjc.code.daw.marketplace.web.sale.dto.UpdateOtdDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class SaleController {

    private final ProductService productService;
    private final SaleService saleService;

    public SaleController(ProductService productService,
                          SaleService saleService) {
        this.productService = productService;
        this.saleService = saleService;
    }

    @RequestMapping(
            path = "/sale/otd/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateOtdSale(@ModelAttribute UpdateOtdDto request, Model model) {

        Date startDate;
        try {
            Integer.parseInt(request.getStartDate().split("/")[0]);
            Integer.parseInt(request.getStartDate().split("/")[1]);
            Integer.parseInt(request.getStartDate().split("/")[2]);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            startDate = formatter.parse(request.getStartDate());
        } catch (Exception e) {
            model.addAttribute("message", "You provided an invalid start date");
            model.addAttribute("danger", "yes");
            return "flash";
        }

        Date stopDate;
        try {
            Integer.parseInt(request.getStartDate().split("/")[0]);
            Integer.parseInt(request.getStartDate().split("/")[1]);
            Integer.parseInt(request.getStartDate().split("/")[2]);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            stopDate = formatter.parse(request.getStopDate());
        } catch (Exception e) {
            model.addAttribute("message", "You provided an invalid stop date");
            model.addAttribute("danger", "yes");
            return "flash";
        }

        int discount;
        try {
            discount = Integer.parseInt(request.getDiscountPercentage());
        } catch (Exception e) {
            model.addAttribute("message", "You provided an invalid discount percentage");
            model.addAttribute("danger", "yes");
            return "flash";
        }

        long productId;
        try {
            productId = Long.parseLong(request.getProductId());
            Optional.ofNullable(productService.findProductById(productId)).orElseThrow();
        } catch (Exception e) {
            model.addAttribute("message", "You provided an invalid product");
            model.addAttribute("danger", "yes");
            return "flash";
        }

        saleService.updateCurrentOtd(startDate, stopDate, discount, productId);

        model.addAttribute("message", "The sale has been successfully saved");
        model.addAttribute("success", "yes");

        return "flash";
    }

}
