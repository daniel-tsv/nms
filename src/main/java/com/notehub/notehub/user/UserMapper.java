package com.notehub.notehub.user;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    User convertToEntity(UserDTO userDTO);

    UserDTO convertToDTO(User user);

    List<User> convertToEntity(List<UserDTO> userDTOs);

    List<UserDTO> convertToDTO(List<User> users);
}
