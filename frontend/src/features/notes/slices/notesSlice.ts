import { PayloadAction, createSlice } from "@reduxjs/toolkit";
import { NoteDetailDTO, NoteSummaryDTO } from "../types";

interface NotesState {
  notes: NoteSummaryDTO[];
  selectedNote: NoteDetailDTO | null;
  isEditingTitle: boolean;
}

const initialState: NotesState = {
  notes: [] as NoteSummaryDTO[],
  selectedNote: null,
  isEditingTitle: false,
};

const notesSlice = createSlice({
  name: "notes",
  initialState,
  reducers: {
    setNoteSummaries: (state, action: PayloadAction<NoteSummaryDTO[]>) => {
      state.notes = action.payload;
    },
    addNoteSummary: (state, action: PayloadAction<NoteSummaryDTO>) => {
      state.notes.push(action.payload);
    },
    removeNoteSummary: (state, action: PayloadAction<string>) => {
      const title = action.payload;
      const index = state.notes.findIndex((summary) => summary.title === title);
      if (index !== -1) state.notes.splice(index, 1);
    },
    setNoteSummaryTitle: (
      state,
      action: PayloadAction<{ title: string; newTitle: string }>
    ) => {
      const { title, newTitle } = action.payload;
      const existingNote = state.notes.find(
        (summary) => summary.title === title
      );
      if (existingNote) existingNote.title = newTitle;
    },
    setSelectedNote: (state, action: PayloadAction<NoteDetailDTO>) => {
      state.selectedNote = action.payload;
    },
    clearSelectedNote: (state) => {
      state.selectedNote = null;
    },
    setSelectedNoteTitle: (state, action: PayloadAction<string>) => {
      const note = state.selectedNote;
      if (note) note.title = action.payload;
    },
    setEditingTitle: (state, action: PayloadAction<boolean>) => {
      state.isEditingTitle = action.payload;
    },
  },
});

export const {
  setNoteSummaries,
  addNoteSummary,
  removeNoteSummary,
  setNoteSummaryTitle,
  clearSelectedNote,
  setSelectedNote,
  setSelectedNoteTitle,
  setEditingTitle,
} = notesSlice.actions;
export default notesSlice.reducer;
