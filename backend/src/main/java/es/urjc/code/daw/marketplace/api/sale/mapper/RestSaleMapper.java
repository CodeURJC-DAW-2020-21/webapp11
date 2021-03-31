package es.urjc.code.daw.marketplace.api.sale.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestSaleMapper {

    RestSaleMapper INSTANCE = Mappers.getMapper(RestSaleMapper.class);

    // Implement me...

}
