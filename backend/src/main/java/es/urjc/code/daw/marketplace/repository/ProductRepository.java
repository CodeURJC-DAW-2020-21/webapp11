package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

    @Query("select distinct p.category from Product p")
    List<String> findAllCategories();

}
