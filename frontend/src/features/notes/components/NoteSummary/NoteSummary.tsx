import { Button } from "../../../../shared/components/Button/Button";
import { Input } from "../../../../shared/components/Input/Input";
import type { NoteSummaryDTO } from "../../types";
import styles from "./NoteSummary.module.css";

interface NoteSummaryProps {
  isSelected: boolean;
  isEditing: boolean;
  note: NoteSummaryDTO;
  onClick: () => void;
  handleSaveTitle: () => void;
  handleEditTitle: () => void;
  handleSetTitle: (newTitle: string) => void;
  handleDeleteNote: (title: string) => void;
  titleHasChanged: boolean;
}

export const NoteSummary = ({
  isSelected,
  isEditing,
  note,
  onClick,
  handleSaveTitle,
  handleEditTitle,
  handleSetTitle,
  handleDeleteNote,
  titleHasChanged,
}: NoteSummaryProps) => {
  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      handleSaveTitle();
    }
  };

  return isSelected ? (
    <div
      className={`${styles["note-summary"]} ${styles["note-summary__active"]}`}
    >
      {isEditing ? (
        <Input
          className={styles["note-title"]}
          value={note.title}
          onChange={(e) => handleSetTitle(e.target.value)}
          autoFocus={true}
          type={"text"}
          placeholder={"Note title"}
          onKeyDown={handleKeyDown}
        />
      ) : (
        <h4 className={styles["note-title"]}>{note.title}</h4>
      )}
      <div className={styles["note-actions"]}>
        {isEditing ? (
          <Button
            iconPath={titleHasChanged ? "/save.svg" : "/undo.svg"}
            onClick={handleSaveTitle}
            className={styles["note-action-button"]}
            variant={titleHasChanged ? "accent" : "secondary"}
          />
        ) : (
          <Button
            className={styles["note-action-button"]}
            onClick={handleEditTitle}
            iconPath="/edit.svg"
            variant={"accent"}
          />
        )}
        <Button
          className={styles["note-action-button"]}
          onClick={() => handleDeleteNote(note.title)}
          variant="danger"
          iconPath="/delete.svg"
        />
      </div>
    </div>
  ) : (
    <Button
      className={`${styles["note-summary"]} ${styles["note-summary__inactive"]}`}
      onClick={onClick}
      label={note.title}
    />
  );
};
