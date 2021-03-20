package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.Product;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();

    List<Pair<String, Integer>> findCategoryToWeeklyPurchases();

    List<Integer> findSalesPerDayInWeek();

    Long findAccumulatedCapital();

    Product findProductById(Long id);

}
