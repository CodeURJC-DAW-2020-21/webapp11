package es.urjc.code.daw.marketplace.api.sale.mapper;

import es.urjc.code.daw.marketplace.api.sale.dto.FindAdResponseDto;
import es.urjc.code.daw.marketplace.api.sale.dto.FindOtdResponseDto;
import es.urjc.code.daw.marketplace.api.sale.dto.UpdateAdSaleRequestDto;
import es.urjc.code.daw.marketplace.api.sale.dto.UpdateOtdSaleRequestDto;
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
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "consumers", ignore = true)
    @Mapping(target = "discountPercentage", source = "discount")
    @Mapping(target = "start", source = "startDate")
    @Mapping(target = "stop", source = "stopDate")
    OneTimeDiscount asOtd(UpdateOtdSaleRequestDto updateOtdSaleDto);
  
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "consumers", ignore = true)
    @Mapping(target = "discountPercentage", source = "discount")
    @Mapping(target = "start", source = "startDate")
    @Mapping(target = "stop", source = "stopDate")
    @Mapping(target = "bulkAmount", source = "amount")
    AccumulativeDiscount asAd(UpdateAdSaleRequestDto updateOtdSaleDto);

    @Mapping(target = "discountedPrice", ignore = true)
    @Mapping(target = "discount", source = "discount.discountPercentage")
    FindOtdResponseDto asFindResponse(OneTimeDiscount discount, Product product);

    @Mapping(target = "discountedPrice", ignore = true)
    @Mapping(target = "discount", source = "discount.discountPercentage")
    FindAdResponseDto asFindResponse(AccumulativeDiscount discount, Product product);

}
