package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import es.urjc.code.daw.marketplace.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @Query("select count(o) from Order o where o.creationDate between :startDate and :endDate")
    Integer countAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select count(o) from Order o where o.product.category = :category and o.creationDate between :startDate and :endDate")
    Integer countAllBetweenDatesByCategory(@Param("category") String category, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select sum(o.finalCost) from Order o")
    Long computeTotalAccumulatedCapital();

    @Query("select count(o) from Order o where o.product.id = :productId and o.user.id = :userId")
    Integer countConcreteProductPurchasesGivenUser(@Param("productId") Long productId, @Param("userId") Long userId);

    @Query("select o from Order o where o.user.id = :userId")
    List<Order> findAllOrdersByUserId(@Param("userId") Long userId);

    @Query("select o from Order o where o.user.id = :userId")
    List<Order> findAllOrdersByUserId(@Param("userId") Long userId, Pageable pageable);

}
