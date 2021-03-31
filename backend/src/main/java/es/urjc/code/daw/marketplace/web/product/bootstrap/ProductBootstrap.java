package es.urjc.code.daw.marketplace.web.product.bootstrap;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
@org.springframework.core.annotation.Order(2)
public class ProductBootstrap implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductBootstrap(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        List<Product> products = Lists.newArrayList(

            // Shared Server Products:

            Product.builder()
                    .category("shared")
                    .price(5)
                    .ram("1 GB")
                    .cores("1 vCPU")
                    .storage("32 GB")
                    .transfer("1 TB")
                    .build(),

            Product.builder()
                    .category("shared")
                    .price(10)
                    .ram("2 GB")
                    .cores("1 vCPU")
                    .storage("64 GB")
                    .transfer("1 TB")
                    .build(),

            Product.builder()
                    .category("shared")
                    .price(20)
                    .ram("4 GB")
                    .cores("2 vCPU")
                    .storage("128 GB")
                    .transfer("2 TB")
                    .build(),

            Product.builder()
                    .category("shared")
                    .price(25)
                    .ram("4 GB")
                    .cores("2 vCPU")
                    .storage("256 GB")
                    .transfer("5 TB")
                    .build(),

            Product.builder()
                    .category("shared")
                    .price(35)
                    .ram("8 GB")
                    .cores("4 vCPU")
                    .storage("512 GB")
                    .transfer("5 TB")
                    .build(),

            // Virtual Private Server Products

            Product.builder()
                    .category("vps")
                    .price(10)
                    .ram("1 GB")
                    .cores("1 vCPU")
                    .storage("128 GB")
                    .transfer("1 TB")
                    .build(),

            Product.builder()
                    .category("vps")
                    .price(15)
                    .ram("2 GB")
                    .cores("2 vCPU")
                    .storage("256 GB")
                    .transfer("2 TB")
                    .build(),

            Product.builder()
                    .category("vps")
                    .price(20)
                    .ram("4 GB")
                    .cores("2 vCPU")
                    .storage("512 GB")
                    .transfer("5 TB")
                    .build(),

            // Dedicated Server Products

            Product.builder()
                    .category("dedicated")
                    .price(60)
                    .ram("16 GB")
                    .cores("6 vCPU")
                    .storage("2 TB")
                    .transfer("25 TB")
                    .build(),

            Product.builder()
                    .category("dedicated")
                    .price(140)
                    .ram("32 GB")
                    .cores("12 vCPU")
                    .storage("5 TB")
                    .transfer("50 TB")
                    .build(),

            Product.builder()
                    .category("dedicated")
                    .price(300)
                    .ram("64 GB")
                    .cores("16 vCPU")
                    .storage("8 TB")
                    .transfer("100 TB")
                    .build()
        );

        productRepository.saveAll(products);

    }

}
