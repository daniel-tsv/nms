package com.notehub.notehub.note;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface NoteMapper {

    Note toEntity(NoteDTO noteDTO);

    NoteDTO toDTO(Note note);

    List<Note> toEntity(List<NoteDTO> noteDTOs);

    List<NoteDTO> toDTO(List<Note> notes);
}
