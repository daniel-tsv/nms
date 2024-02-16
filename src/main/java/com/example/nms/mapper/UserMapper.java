package com.example.nms.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.service.note.NoteService;

@Mapper(componentModel = "spring", uses = NoteService.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    private NoteService noteService;

    public abstract User toEntity(UserDTO userDTO);

    public abstract UserDTO toDTO(User user);

    public abstract List<User> toEntity(List<UserDTO> userDTOs);

    public abstract List<UserDTO> toDTO(List<User> users);

    @AfterMapping
    protected void setNumberOfNotes(User user, @MappingTarget UserDTO userDTO) {
        userDTO.setNumberOfNotes(noteService.getUserNotesCount(user));
    }
}
