package es.urjc.code.daw.marketplace.api.sale.mapper;

import es.urjc.code.daw.marketplace.api.sale.dto.FindAdResponseDto;
import es.urjc.code.daw.marketplace.api.sale.dto.FindOtdResponseDto;
import es.urjc.code.daw.marketplace.api.sale.dto.UpdateSaleRequestDto;
import es.urjc.code.daw.marketplace.domain.AccumulativeDiscount;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestSaleMapper {

    RestSaleMapper INSTANCE = Mappers.getMapper(RestSaleMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consumers", ignore = true)
    @Mapping(target = "discountPercentage", source = "discount")
    @Mapping(target = "start", source = "startDate")
    @Mapping(target = "stop", source = "stopDate")
    @Mapping(target = "enabled", source = "isEnabled")
    OneTimeDiscount asOtd(UpdateSaleRequestDto updateOtdSaleDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consumers", ignore = true)
    @Mapping(target = "discountPercentage", source = "discount")
    @Mapping(target = "start", source = "startDate")
    @Mapping(target = "stop", source = "stopDate")
    @Mapping(target = "bulkAmount", source = "amount")
    @Mapping(target = "enabled", source = "isEnabled")
    AccumulativeDiscount asAd(UpdateSaleRequestDto updateOtdSaleDto);

    @Mapping(target = "discountedProductId", source = "product.id")
    @Mapping(target = "startDate", source = "discount.start")
    @Mapping(target = "discountedPrice", ignore = true)
    @Mapping(target = "discount", source = "discount.discountPercentage")
    @Mapping(target = "expiryDate", source = "discount.stop")
    FindOtdResponseDto asFindResponse(OneTimeDiscount discount, Product product);

    @Mapping(target = "discountedProductId", source = "product.id")
    @Mapping(target = "startDate", source = "discount.start")
    @Mapping(target = "discountedPrice", ignore = true)
    @Mapping(target = "discount", source = "discount.discountPercentage")
    @Mapping(target = "expiryDate", source = "discount.stop")
    FindAdResponseDto asFindResponse(AccumulativeDiscount discount, Product product);

}
