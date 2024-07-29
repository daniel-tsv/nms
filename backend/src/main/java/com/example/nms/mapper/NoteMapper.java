package com.example.nms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.dto.NoteSummaryDTO;
import com.example.nms.entity.Note;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoteMapper {

    Note toEntity(NoteDetailDTO noteDTO);

    NoteDetailDTO toDetailDTO(Note note);

    List<Note> toEntityFromDetailDTO(List<NoteDetailDTO> noteDTOs);

    List<NoteDetailDTO> toDetailDTO(List<Note> notes);

    Note toEntity(NoteSummaryDTO noteDTO);

    NoteSummaryDTO toSummaryDTO(Note note);

    List<Note> toEntity(List<NoteSummaryDTO> noteDTOs);

    List<NoteSummaryDTO> toSummaryDTO(List<Note> notes);
}
