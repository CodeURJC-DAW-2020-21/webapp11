package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;

public interface OtdRepository extends PagingAndSortingRepository<OneTimeDiscount, Long> {

    @Query(value = "SELECT * FROM one_time_discounts WHERE enabled = true ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<OneTimeDiscount> findCurrentlyActiveOtd();

    @Modifying
    @Query(value = "UPDATE OneTimeDiscount otd SET otd.enabled = false")
    void disableActiveOtd();

}
