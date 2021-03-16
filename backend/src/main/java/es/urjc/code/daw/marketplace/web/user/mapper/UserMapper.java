package es.urjc.code.daw.marketplace.web.user.mapper;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.web.user.dto.RegisterUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.dto.UpdateUserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// Map struct documentation
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "profilePictureFilename", ignore = true)
    @Mapping(target = "accumulativeDiscountsConsumed", ignore = true)
    @Mapping(target = "oneTimeDiscountsConsumed", ignore = true)
    User asRegisterUser(RegisterUserRequestDto request);


    @Mapping(target = "id", ignore=true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "accumulativeDiscountsConsumed", ignore = true)
    @Mapping(target = "oneTimeDiscountsConsumed", ignore = true)
    User asUpdateUser(UpdateUserRequestDto request);

}