package com.notehub.notehub.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.notehub.notehub.dto.NoteDTO;

import entities.Note;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoteMapper {

    Note toEntity(NoteDTO noteDTO);

    NoteDTO toDTO(Note note);

    List<Note> toEntity(List<NoteDTO> noteDTOs);

    List<NoteDTO> toDTO(List<Note> notes);
}
