package es.urjc.code.daw.marketplace.web.sale.bootstrap;

import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.repository.AdRepository;
import es.urjc.code.daw.marketplace.repository.OtdRepository;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Component
@Transactional
@org.springframework.core.annotation.Order(3)
public class SaleBootstrap implements CommandLineRunner {

    private final OtdRepository otdRepository;
    private final AdRepository adRepository;
    private final ProductRepository productRepository;

    public SaleBootstrap(OtdRepository otdRepository,
                         AdRepository adRepository,
                         ProductRepository productRepository) {
        this.otdRepository = otdRepository;
        this.adRepository = adRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        Optional<Product> optionalProduct = productRepository.findById(1L);
        Product product = optionalProduct.orElseThrow();

        OneTimeDiscount otd = OneTimeDiscount.builder()
                .productId(product.getId())
                .discountPercentage(50)
                .stop(new Date(System.currentTimeMillis() + Integer.MAX_VALUE))
            .build();

        otdRepository.save(otd);

        AccumulativeDiscount ad = AccumulativeDiscount.builder()
                .productId(product.getId())
                .discountPercentage(25)
                .bulkAmount(10)
                .stop(new Date(System.currentTimeMillis() + Integer.MAX_VALUE))
            .build();

        adRepository.save(ad);

    }

}
