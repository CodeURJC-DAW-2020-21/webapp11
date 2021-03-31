package es.urjc.code.daw.marketplace.api.statistics.mapper;

import es.urjc.code.daw.marketplace.api.sale.mapper.RestSaleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestStatisticsMapper {

    RestStatisticsMapper INSTANCE = Mappers.getMapper(RestStatisticsMapper.class);

    // Implement me...

}
