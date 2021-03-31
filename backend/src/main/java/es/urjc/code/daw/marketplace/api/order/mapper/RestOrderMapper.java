package es.urjc.code.daw.marketplace.api.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestOrderMapper {

    RestOrderMapper INSTANCE = Mappers.getMapper(RestOrderMapper.class);

    // Implement me...

}
