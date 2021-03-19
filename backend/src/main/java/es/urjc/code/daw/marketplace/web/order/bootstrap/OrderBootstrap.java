package es.urjc.code.daw.marketplace.web.order.bootstrap;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.OrderRepository;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.repository.UserRepository;
import es.urjc.code.daw.marketplace.security.SecurityProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
@org.springframework.core.annotation.Order(3)
public class OrderBootstrap implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SecurityProperties securityProperties;

    public OrderBootstrap(ProductRepository productRepository,
                          OrderRepository orderRepository,
                          UserRepository userRepository,
                          SecurityProperties securityProperties) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.securityProperties = securityProperties;
    }

    @SuppressWarnings("all")
    @Override
    public void run(String... args) {
        User user = userRepository.findUserByEmail(securityProperties.getClientEmail());
        Iterable<Product> products = productRepository.findAll();

        List<Order> orders = Lists.newArrayList();
        for(int i = 0; i < 3; i++) {
            for(Product product : products) {
                orders.add(
                    Order.builder()
                        .product(product)
                        .user(user)
                        .finalCost(product.getPrice())
                    .build()
                );
            }
        }

        orderRepository.saveAll(orders);
    }

}
