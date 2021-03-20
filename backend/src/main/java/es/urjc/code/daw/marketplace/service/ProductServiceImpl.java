package es.urjc.code.daw.marketplace.service;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.repository.OrderRepository;
import es.urjc.code.daw.marketplace.repository.ProductRepository;
import es.urjc.code.daw.marketplace.util.TimeUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Product> findAllProducts() {
        return Lists.newArrayList(productRepository.findAll());
    }

    @Override
    public List<Pair<String, Integer>> findCategoryToWeeklyPurchases() {
        Date firstDayOfWeek = TimeUtils.firstDayOfCurrentWeek();
        Date lastDayOfWeek = TimeUtils.sumDaysToDate(firstDayOfWeek, 7);
        lastDayOfWeek = TimeUtils.removeSecondsFromDate(lastDayOfWeek, 1);

        List<String> categories = productRepository.findAllCategories();
        List<Pair<String, Integer>> categoryToPurchases = new LinkedList<>();

        for(String category : categories) {
            int categoryPurchasesThisWeek = orderRepository.countAllBetweenDatesByCategory(category, firstDayOfWeek, lastDayOfWeek);
            categoryToPurchases.add(new ImmutablePair<>(category, categoryPurchasesThisWeek));
        }

        return categoryToPurchases;
    }

    @Override
    public List<Integer> findSalesPerDayInWeek() {
        List<Integer> salesPerDayInWeek = new LinkedList<>();
        Date startDate = TimeUtils.firstDayOfCurrentWeek();
        for(int dayIncrement = 0; dayIncrement < 7; dayIncrement++) {
            Date endDate = TimeUtils.sumDaysToDate(startDate, 1);
            Date exclusiveEndDate = TimeUtils.removeSecondsFromDate(endDate, 1);
            Integer count = orderRepository.countAllBetweenDates(startDate, exclusiveEndDate);
            salesPerDayInWeek.add(count);
            startDate = endDate;
        }
        return salesPerDayInWeek;
    }

    @Override
    public Long findAccumulatedCapital() {
        Long capital = orderRepository.computeTotalAccumulatedCapital();
        return capital != null ? capital : 0;
    }

    @Override
    public Product findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow();
    }

}