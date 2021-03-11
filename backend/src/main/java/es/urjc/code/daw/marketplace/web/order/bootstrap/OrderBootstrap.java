package es.urjc.code.daw.marketplace.web.order.bootstrap;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.repository.OrderRepository;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
@org.springframework.core.annotation.Order(2)
public class OrderBootstrap implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderBootstrap(ProductRepository productRepository,
                          OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {

        List<Order> orders = Lists.newArrayList(

            Order.builder()
                    .product(productRepository.findAll().iterator().next())
                .build()

        );

        orderRepository.saveAll(orders);

    }

}
