import { api } from "../../../shared/util/api";
import { PaginationOptions, SearchType } from "../../search/types";
import type { NoteDetailDTO, NotesPageDTO, NoteSummaryDTO } from "../types";

export const NoteService = {
  async createNote(note: NoteSummaryDTO | NoteDetailDTO) {
    return api.post(`/notes`, note);
  },

  async getNotes(pagination: PaginationOptions) {
    const { page, size, direction, sortBy } = pagination;
    const response = await api.get<NotesPageDTO>(
      `/notes?page=${page}&size=${size}&direction=${direction}&sortBy=${sortBy}`
    );
    return response.data;
  },

  async getNoteByTitle(title: string) {
    const response = await api.get<NoteDetailDTO>(`/notes/title/${title}`);
    return response.data;
  },

  async searchNotes(
    term: string,
    searchType: SearchType,
    pagination: PaginationOptions
  ) {
    const { page, size, direction, sortBy } = pagination;
    const response = await api.get<NotesPageDTO>(
      `/notes/search?term=${term}&searchInContents=${Boolean(
        searchType === SearchType.CONTENT
      )}&page=${page}&size=${size}&direction=${direction}&sortBy=${sortBy}`
    );
    return response.data;
  },

  async updateNote(title: string, note: NoteSummaryDTO | NoteDetailDTO) {
    const response = await api.patch<NoteDetailDTO>(
      `/notes/title/${title}`,
      note
    );
    return response.data;
  },

  async deleteNote(title: string) {
    const response = await api.delete(`/notes/title/${title}`);
    return response.data;
  },
};
