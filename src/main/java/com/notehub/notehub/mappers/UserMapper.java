package com.notehub.notehub.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    List<User> toEntity(List<UserDTO> userDTOs);

    List<UserDTO> toDTO(List<User> users);
}
