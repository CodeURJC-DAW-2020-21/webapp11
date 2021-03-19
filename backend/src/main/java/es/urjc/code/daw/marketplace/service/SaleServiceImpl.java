package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.*;
import org.apache.commons.lang3.math.NumberUtils;
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
        if(!isEligibleForCurrentOtd(order.getUser().getId(), order.getProduct().getId())) return;
        Optional<OneTimeDiscount> optionalCurrentOtd = otdRepository.findCurrentlyActiveOtd();
        if(optionalCurrentOtd.isEmpty()) return;
        OneTimeDiscount currentOtd = optionalCurrentOtd.get();
        currentOtd.getConsumers().add(order.getUser());
        otdRepository.saveAndFlush(currentOtd);
        int discountPercentage = currentOtd.getDiscountPercentage();
        int orderCost = order.getFinalCost();
        int finalCost = ((100 - discountPercentage) * orderCost) / 100;
        order.setFinalCost(finalCost);
    }

    @Override
    public void applyAdDiscount(Order order) {
        Optional<AccumulativeDiscount> optionalCurrentAd = adRepository.findCurrentlyActiveAd();
        if(optionalCurrentAd.isEmpty()) return;
        AccumulativeDiscount currentAd = optionalCurrentAd.get();
        currentAd.getConsumers().add(order.getUser());
        adRepository.saveAndFlush(currentAd);
        int discountPercentage = currentAd.getDiscountPercentage();
        int orderCost = order.getFinalCost();
        int finalCost = ((100 - discountPercentage) * orderCost) / 100;
        order.setFinalCost(finalCost);
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
    public void updateCurrentOtd(Date start, Date stop, int discount, long productId) {
        disableCurrentOtd();

        OneTimeDiscount otd = OneTimeDiscount.builder()
                .productId(productId)
                .start(start)
                .stop(stop)
                .discountPercentage(discount)
            .build();

        otdRepository.saveAndFlush(otd);
    }

    @Override
    public void disableCurrentOtd() {
        otdRepository.disableActiveOtd();
    }

    @Override
    public void updateCurrentAd(Date start, Date stop, int discount, long productId, int bulkAmount) {
        disableCurrentAd();

        AccumulativeDiscount ad = AccumulativeDiscount.builder()
                .productId(productId)
                .start(start)
                .stop(stop)
                .discountPercentage(discount)
                .bulkAmount(bulkAmount)
            .build();

        adRepository.saveAndFlush(ad);

    }

    @Override
    public void disableCurrentAd() {
        adRepository.disableActiveAd();
    }

}