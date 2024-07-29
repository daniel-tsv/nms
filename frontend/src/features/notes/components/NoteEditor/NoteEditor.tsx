import { ReactNode } from "react";
import type { NoteDetailDTO } from "../../types";
import styles from "./NoteEditor.module.css";

export const NoteEditor = ({
  selectedNote,
  handleContentChange,
  children,
}: {
  selectedNote: NoteDetailDTO | null;
  handleContentChange: (newContent: string) => void;
  children: ReactNode;
}) => {
  return (
    <div className={styles["note-editor"]}>
      {children}
      {selectedNote && (
        <textarea
          className={styles["text-area"]}
          value={selectedNote.contents}
          onChange={(e) => handleContentChange(e.target.value)}
        />
      )}
    </div>
  );
};
