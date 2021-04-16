package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.*;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OtdRepository otdRepository;
    private final AdRepository adRepository;

    public SaleServiceImpl(UserRepository userRepository,
                           OrderRepository orderRepository,
                           OtdRepository otdRepository,
                           AdRepository adRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.otdRepository = otdRepository;
        this.adRepository = adRepository;
    }

    @Override
    public boolean isEligibleForCurrentOtd(Long userId, Long productId) {
        Optional<OneTimeDiscount> optionalCurrentOtd = otdRepository.findCurrentlyActiveOtd();
        if(optionalCurrentOtd.isEmpty()) return false;
        OneTimeDiscount currentOtd = optionalCurrentOtd.get();
        User user = userRepository.findUserById(userId);
        boolean hasNotConsumedDiscount = !currentOtd.getConsumers().contains(user);
        int userProductPurchases = orderRepository.countConcreteProductPurchasesGivenUser(currentOtd.getProductId(), userId);
        return hasNotConsumedDiscount && userProductPurchases == 0;
    }

    @Override
    public boolean isEligibleForCurrentAd(Long userId, Long productId) {
        Optional<AccumulativeDiscount> optionalCurrentAd = adRepository.findCurrentlyActiveAd();
        if(optionalCurrentAd.isEmpty()) return false;
        AccumulativeDiscount currentAd = optionalCurrentAd.get();
        int userProductPurchases = orderRepository.countConcreteProductPurchasesGivenUser(currentAd.getProductId(), userId);
        return userProductPurchases >= currentAd.getBulkAmount();
    }

    @Override
    public void applyOtdDiscount(Order order) {
        boolean isEligible = isEligibleForCurrentOtd(order.getUser().getId(), order.getProduct().getId());
        if(!isEligible) return;
        OneTimeDiscount discount = otdRepository.findCurrentlyActiveOtd().orElseThrow();
        discount.addConsumer(order.getUser());
        otdRepository.saveAndFlush(discount);
        order.applyDiscount(discount.getDiscountPercentage());
    }

    @Override
    public void applyAdDiscount(Order order) {
        boolean isEligible = isEligibleForCurrentAd(order.getUser().getId(), order.getProduct().getId());
        if(!isEligible) return;
        AccumulativeDiscount discount = adRepository.findCurrentlyActiveAd().orElseThrow();
        discount.addConsumer(order.getUser());
        adRepository.saveAndFlush(discount);
        order.applyDiscount(discount.getDiscountPercentage());
    }

    @Override
    public Optional<OneTimeDiscount> getCurrentOtd() {
        return otdRepository.findCurrentlyActiveOtd();
    }

    @Override
    public Optional<AccumulativeDiscount> getCurrentAd() {
        return adRepository.findCurrentlyActiveAd();
    }

    @Override
    public void updateCurrentOtd(OneTimeDiscount discount) {
        disableCurrentOtd();
        if(discount.getEnabled()) otdRepository.saveAndFlush(discount);
    }

    @Override
    public void disableCurrentOtd() {
        otdRepository.disableActiveOtd();
    }

    @Override
    public void updateCurrentAd(AccumulativeDiscount discount) {
        disableCurrentAd();
        if(discount.getEnabled()) adRepository.saveAndFlush(discount);
    }

    @Override
    public void disableCurrentAd() {
        adRepository.disableActiveAd();
    }

}