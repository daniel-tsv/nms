package com.example.nms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    List<User> toEntity(List<UserDTO> userDTOs);

    List<UserDTO> toDTO(List<User> users);
}
