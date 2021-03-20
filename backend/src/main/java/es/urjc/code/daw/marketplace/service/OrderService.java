package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.Order;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {

    Order findOrderById(Long id);

    List<Order> findAllOrdersByUserId(Long userId);

    List<Order> findAllOrdersByUserId(Long userId, Pageable pageable);

    Order saveOrder(Order order);

}
