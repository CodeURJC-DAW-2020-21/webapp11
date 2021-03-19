package es.urjc.code.daw.marketplace.repository;

import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AdRepository extends JpaRepository<AccumulativeDiscount, Long>, PagingAndSortingRepository<AccumulativeDiscount, Long> {

    @Query(value = "SELECT * FROM accumulative_discounts WHERE enabled = true ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<AccumulativeDiscount> findCurrentlyActiveAd();

    @Modifying
    @Query(value = "UPDATE AccumulativeDiscount ad SET ad.enabled = false")
    void disableActiveAd();

}
