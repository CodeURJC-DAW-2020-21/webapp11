package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
