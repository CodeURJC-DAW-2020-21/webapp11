package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> findAllProducts();

    Map<String, Integer> findCategoryToWeeklyPurchases();

    List<Integer> findSalesPerDayInWeek();

    Long findAccumulatedCapital();

}
