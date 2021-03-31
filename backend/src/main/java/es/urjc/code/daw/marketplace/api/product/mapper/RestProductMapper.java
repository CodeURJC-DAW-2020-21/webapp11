package es.urjc.code.daw.marketplace.api.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestProductMapper {

    RestProductMapper INSTANCE = Mappers.getMapper(RestProductMapper.class);

    // Implement me...

}
