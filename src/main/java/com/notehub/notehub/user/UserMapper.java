package com.notehub.notehub.user;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    List<User> toEntity(List<UserDTO> userDTOs);

    List<UserDTO> toDTO(List<User> users);
}
