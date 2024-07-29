package com.example.nms.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotesPageDTO {
  List<NoteSummaryDTO> notes;
  int currentPage;
  long totalElements;
  int totalPages;
}