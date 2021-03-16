package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AdRepository extends PagingAndSortingRepository<AccumulativeDiscount, Long> {

    @Query(value = "SELECT * FROM accumulative_discounts WHERE enabled = true ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<AccumulativeDiscount> findCurrentlyActiveAd();

    @Modifying
    @Query(value = "UPDATE AccumulativeDiscount ad SET ad.enabled = false")
    void disableActiveAd();

}
