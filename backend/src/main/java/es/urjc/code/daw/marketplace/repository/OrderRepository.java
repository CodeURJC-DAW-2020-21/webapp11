package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @Query("select count(distinct o) from Order o where o.creationDate between :startDate and :endDate")
    Integer countAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select count(distinct o) from Order o where o.product.category = :category and o.creationDate between :startDate and :endDate")
    Integer countAllBetweenDatesByCategory(@Param("category") String category, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select sum(o.product.price) from Order o")
    Long computeTotalAccumulatedCapital();

}
