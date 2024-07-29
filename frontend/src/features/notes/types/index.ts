export interface NoteSummaryDTO {
  uuid?: string;
  title: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface NoteDetailDTO {
  uuid?: string;
  title: string;
  contents: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface NotesPageDTO {
  notes: NoteSummaryDTO[];
  currentPage: number;
  totalElements: number;
  totalPages: number;
}
