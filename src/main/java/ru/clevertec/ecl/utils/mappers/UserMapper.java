package ru.clevertec.ecl.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.models.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping( target = "name", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserDto convertUserToDto(User user);
    List<UserDto> convertUsersToDtos(List<User> users);
}
