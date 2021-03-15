package es.urjc.code.daw.marketplace.service;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.OrderRepository;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.util.TimeUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<Order> findAllOrdersByUserId(Long userId, Pageable pageable) {
        return orderRepository.findAllOrdersByUserId(userId, pageable);
    }

}