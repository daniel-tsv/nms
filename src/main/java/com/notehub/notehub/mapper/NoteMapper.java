package com.notehub.notehub.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.notehub.notehub.dto.NoteDTO;
import com.notehub.notehub.entity.Note;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoteMapper {

    Note toEntity(NoteDTO noteDTO);

    NoteDTO toDTO(Note note);

    List<Note> toEntity(List<NoteDTO> noteDTOs);

    List<NoteDTO> toDTO(List<Note> notes);
}
