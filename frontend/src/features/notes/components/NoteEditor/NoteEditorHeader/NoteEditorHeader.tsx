import { ReactNode } from "react";
import { Button } from "../../../../../shared/components/Button/Button";
import type { NoteDetailDTO } from "../../../types";
import styles from "./NoteEditorHeader.module.css";
export const NoteEditorHeader = ({
  selectedNote,
  handleSaveNoteContents,
  contentsHasChanged,
  children,
}: {
  selectedNote: NoteDetailDTO | null;
  handleSaveNoteContents: (note: NoteDetailDTO) => void;
  contentsHasChanged: boolean;
  children?: ReactNode;
}) => {
  return (
    <div className={styles["header"]}>
      <div className={styles["note-controls"]}>
        <h1 className={styles["note-editor-header-title"]}>
          {selectedNote?.title ?? "Select note to edit, or create new one"}
        </h1>
        <div className={styles["note-editor-actions"]}>
          {selectedNote && (
            <Button
              className={styles["save-button"]}
              iconPath="/save.svg"
              label="Save"
              onClick={() => handleSaveNoteContents(selectedNote)}
              variant={contentsHasChanged ? "accent" : "secondary"}
            />
          )}
        </div>
      </div>
      <div className={styles.children}>{children}</div>
    </div>
  );
};
