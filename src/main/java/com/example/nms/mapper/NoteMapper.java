package com.example.nms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.nms.dto.NoteDTO;
import com.example.nms.entity.Note;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoteMapper {

    Note toEntity(NoteDTO noteDTO);

    NoteDTO toDTO(Note note);

    List<Note> toEntity(List<NoteDTO> noteDTOs);

    List<NoteDTO> toDTO(List<Note> notes);
}
