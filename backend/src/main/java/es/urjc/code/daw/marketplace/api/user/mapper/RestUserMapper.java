package es.urjc.code.daw.marketplace.api.user.mapper;

import es.urjc.code.daw.marketplace.api.user.dto.FindUserResponseDto;
import es.urjc.code.daw.marketplace.api.user.dto.RegisterUserRequestDto;
import es.urjc.code.daw.marketplace.api.user.dto.UpdateUserRequestDto;
import es.urjc.code.daw.marketplace.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestUserMapper {

    RestUserMapper INSTANCE = Mappers.getMapper(RestUserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isLocked", ignore = true)
    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "profilePictureFilename", ignore = true)
    @Mapping(target = "accumulativeDiscountsConsumed", ignore = true)
    @Mapping(target = "oneTimeDiscountsConsumed", ignore = true)
    User asRegisterUser(RegisterUserRequestDto request);


    @Mapping(target = "id", ignore=true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "accumulativeDiscountsConsumed", ignore = true)
    @Mapping(target = "oneTimeDiscountsConsumed", ignore = true)
    @Mapping(target="isEnabled", source="enabled")
    User asUpdateUser(UpdateUserRequestDto request);

    FindUserResponseDto asFindUserResponse(User user);

}