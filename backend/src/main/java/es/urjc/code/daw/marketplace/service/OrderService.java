package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.Order;

public interface OrderService {

    Order findOrderById(Long id);

}
