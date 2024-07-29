import { type ReactNode } from "react";
import { Loading } from "../../../../shared/components/Loading/Loading";
import type { NoteDetailDTO, NoteSummaryDTO } from "../../types";
import { NoteSummary } from "../NoteSummary/NoteSummary";
import styles from "./NotesList.module.css";

interface NotesListState {
  isUpdating: boolean;
  notes: NoteSummaryDTO[];
  isEditingTitle: boolean;
  selectedNote: NoteDetailDTO | null;
  previousNoteTitle: string;
  handleSelectNote: (title: string) => void;
  handleSaveNoteSummaryTitle(title: string, newTitle: string): void;
  handleEditTitle: (title: string) => void;
  handleSetTitle: (title: string, newTitle: string) => void;
  handleDeleteNote: (title: string) => void;
  children?: ReactNode;
}

export const NotesList = ({
  isUpdating,
  notes,
  isEditingTitle,
  selectedNote,
  previousNoteTitle,
  handleSelectNote,
  handleSaveNoteSummaryTitle,
  handleEditTitle,
  handleSetTitle,
  handleDeleteNote,
  children,
}: NotesListState) => {
  return isUpdating ? (
    <Loading />
  ) : (
    <div className={styles["notes-list"]}>
      {children}
      {notes.map((note) => (
        <NoteSummary
          key={`${note.title} ${note.createdAt}`}
          note={note}
          isSelected={
            note.title === selectedNote?.title &&
            note.createdAt === selectedNote?.createdAt
          }
          isEditing={isEditingTitle}
          onClick={() => handleSelectNote(note.title)}
          handleSaveTitle={() =>
            handleSaveNoteSummaryTitle(previousNoteTitle, note.title)
          }
          handleEditTitle={() => handleEditTitle(note.title)}
          handleSetTitle={(newTitle) => handleSetTitle(note.title, newTitle)}
          handleDeleteNote={() => handleDeleteNote(previousNoteTitle)}
          titleHasChanged={note.title !== previousNoteTitle}
        />
      ))}
    </div>
  );
};
