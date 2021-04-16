package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Order;
import java.util.Date;
import java.util.Optional;

public interface SaleService {

    boolean isEligibleForCurrentOtd(Long userId, Long productId);

    void applyOtdDiscount(Order order);

    Optional<OneTimeDiscount> getCurrentOtd();

    boolean isEligibleForCurrentAd(Long userId, Long productId);

    void applyAdDiscount(Order order);

    Optional<AccumulativeDiscount> getCurrentAd();

    void updateCurrentOtd(OneTimeDiscount discount);

    void disableCurrentOtd();

    void updateCurrentAd(AccumulativeDiscount discount);

    void disableCurrentAd();

}
