package com.example.nms.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.nms.dto.AdminUserDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.service.note.NoteService;

@Mapper(componentModel = "spring", uses = NoteService.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @SuppressWarnings("java:S6813")
    @Autowired
    private NoteService noteService;

    public abstract User toEntity(UserDTO userDTO);

    public abstract UserDTO toDTO(User user);

    public abstract List<User> toEntity(List<UserDTO> userDTOs);

    public abstract List<UserDTO> toDTO(List<User> users);

    public abstract User toEntity(AdminUserDTO adminUserDTO);

    public abstract AdminUserDTO toAdminUserDTO(User user);

    public abstract List<User> toEntityFromAdminUserDTO(List<AdminUserDTO> adminUserDTOs);

    public abstract List<AdminUserDTO> toAdminUserDTO(List<User> user);

    @AfterMapping
    protected void setNumberOfNotes(User user, @MappingTarget UserDTO userDTO) {
        userDTO.setNumberOfNotes(noteService.getUserNotesCount(user));
    }

    @AfterMapping
    protected void setNumberOfNotesForAdmin(User user, @MappingTarget AdminUserDTO userDTO) {
        userDTO.setNumberOfNotes(noteService.getUserNotesCount(user));
    }

    public User updateUserFromDTO(User existingUser, UserDTO updatedUserDTO) {
        existingUser.setUsername(updatedUserDTO.getUsername());
        existingUser.setEmail(updatedUserDTO.getEmail());
        return existingUser;
    }

    public User updateUserFromAdminDTO(User existingUser, AdminUserDTO updatedUserDTO) {
        Optional.ofNullable(updatedUserDTO.getIsAccountNonExpired())
                .ifPresent(existingUser::setIsAccountNonExpired);
        Optional.ofNullable(updatedUserDTO.getIsAccountNonLocked())
                .ifPresent(existingUser::setIsAccountNonLocked);
        Optional.ofNullable(updatedUserDTO.getIsCredentialsNonExpired())
                .ifPresent(existingUser::setIsCredentialsNonExpired);
        Optional.ofNullable(updatedUserDTO.getIsEnabled())
                .ifPresent(existingUser::setIsEnabled);
        return existingUser;
    }
}
