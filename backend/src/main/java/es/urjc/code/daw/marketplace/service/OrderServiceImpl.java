package es.urjc.code.daw.marketplace.service;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.repository.OrderRepository;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.util.TimeUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order findOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElseThrow();
    }

    @Override
    public List<Order> findAllOrdersByUserId(Long userId) {
        return orderRepository.findAllOrdersByUserId(userId);
    }

}