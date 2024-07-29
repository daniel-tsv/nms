import { useState } from "react";
import { Button } from "../../../../shared/components/Button/Button";
import { Input } from "../../../../shared/components/Input/Input";
import styles from "./UserProfile.module.css";

interface UserProfileProps {
  username: string;
  email: string;
  notesCount: number;
  handleSave: (newUsername: string, newEmail: string) => void;
  handleDelete: () => void;
  handleLogout: () => void;
}

export const UserProfile = ({
  username,
  email,
  notesCount,
  handleSave,
  handleDelete,
  handleLogout,
}: UserProfileProps) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [newUsername, setNewUsername] = useState(username);
  const [newEmail, setNewEmail] = useState(email);

  const handleExpand = () => setIsExpanded(!isExpanded);
  const handleEdit = () => setIsEditing(true);
  const handleCancel = () => {
    setIsEditing(false);
    setNewUsername(username);
    setNewEmail(email);
  };
  const handleSaveChanges = () => {
    if (newUsername === username && newEmail === email) {
      setIsEditing(false);
      return;
    }
    handleSave(newUsername, newEmail);
    setIsEditing(false);
  };

  return (
    <div className={styles["profile-container"]}>
      {isExpanded ? (
        <div className={styles["profile-container__expanded"]}>
          {isEditing ? (
            <>
              <Input
                value={newUsername}
                onChange={(e) => setNewUsername(e.target.value)}
                placeholder="Username"
                className={styles["profile-container__input"]}
              />
              <Input
                value={newEmail}
                onChange={(e) => setNewEmail(e.target.value)}
                placeholder="Email"
                className={styles["profile-container__input"]}
              />
              <div className={styles["profile-container__actions"]}>
                <Button
                  onClick={handleSaveChanges}
                  label="Save"
                  iconPath="/save.svg"
                  className={`${styles["profile-container__button"]} ${
                    newUsername !== username || newEmail !== email
                      ? styles["profile-container__button--active"]
                      : styles["profile-container__button--inactive"]
                  }`}
                />
                <Button
                  className={`${styles["profile-container__button"]} ${styles["profile-container__button--cancel"]}`}
                  onClick={handleCancel}
                  label="Cancel"
                />
              </div>
            </>
          ) : (
            <>
              <p className={styles["profile-container__text"]}>
                Username: {username}
              </p>
              <p className={styles["profile-container__text"]}>
                Email: {email}
              </p>
              <p className={styles["profile-container__text"]}>
                Notes: {notesCount}
              </p>
              <div className={styles["profile-container__actions"]}>
                <Button
                  onClick={handleExpand}
                  className={`${styles["profile-container__button"]} ${styles["profile-container__button--hide"]}`}
                  label="Hide"
                  iconPath="/hide.svg"
                />
                <Button
                  onClick={handleEdit}
                  label="Edit"
                  className={styles["profile-container__button"]}
                  iconPath="/edit.svg"
                />
              </div>
            </>
          )}

          <Button
            onClick={handleLogout}
            className={`${styles["profile-container__button"]} ${styles["profile-container__button--logout"]}`}
            label="Logout"
            iconPath="/logout.svg"
          />
          <Button
            onClick={handleDelete}
            label="Delete Profile"
            variant="danger"
            className={`${styles["profile-container__button"]} ${styles["profile-container__button--danger"]}`}
            iconPath="/delete.svg"
          />
        </div>
      ) : (
        <button
          className={styles["profile-container__bubble"]}
          onClick={handleExpand}
        />
      )}
    </div>
  );
};
