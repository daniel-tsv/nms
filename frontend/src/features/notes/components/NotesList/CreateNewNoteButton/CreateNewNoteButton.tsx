import { Button } from "../../../../../shared/components/Button/Button";
import styles from "./CreateNewNoteButton.module.css";

export const CreateNewNoteButton = ({
  handleCreateNote,
}: {
  handleCreateNote: () => void;
}) => {
  return (
    <Button
      onClick={handleCreateNote}
      iconPath="/create.svg"
      label={"Create new note"}
      variant={"accent"}
      className={styles["create-new-note-button"]}
    />
  );
};
