package es.urjc.code.daw.marketplace.api.sale.mapper;

import es.urjc.code.daw.marketplace.api.sale.dto.FindOtdResponseDto;
import es.urjc.code.daw.marketplace.domain.OneTimeDiscount;
import es.urjc.code.daw.marketplace.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestSaleMapper {

    RestSaleMapper INSTANCE = Mappers.getMapper(RestSaleMapper.class);


    @Mapping(target = "discountedPrice", ignore = true)
    @Mapping(target = "discount", source = "discount.discountPercentage")
    FindOtdResponseDto asFindResponse(OneTimeDiscount discount, Product product);


}
