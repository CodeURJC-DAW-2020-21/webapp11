package es.urjc.code.daw.marketplace.api.product.mapper;

import es.urjc.code.daw.marketplace.api.product.dto.FindProductResponseDto;
import es.urjc.code.daw.marketplace.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestProductMapper {

    RestProductMapper INSTANCE = Mappers.getMapper(RestProductMapper.class);

    FindProductResponseDto asFindResponse(Product product);

}
