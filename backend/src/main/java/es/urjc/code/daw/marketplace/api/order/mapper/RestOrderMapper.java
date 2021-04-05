package es.urjc.code.daw.marketplace.api.order.mapper;

import es.urjc.code.daw.marketplace.api.order.dto.FindOrderResponseDto;
import es.urjc.code.daw.marketplace.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestOrderMapper {

    RestOrderMapper INSTANCE = Mappers.getMapper(RestOrderMapper.class);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "category", source = "product.category")
    @Mapping(target = "purchase", source = "creationDate")
    @Mapping(target = "expiration", source = "expiryDate")
    @Mapping(target = "ram", source = "product.ram")
    @Mapping(target = "cores", source = "product.cores")
    @Mapping(target = "storage", source = "product.storage")
    @Mapping(target = "transfer", source = "product.transfer")
    FindOrderResponseDto asFindResponse(Order order);

}
