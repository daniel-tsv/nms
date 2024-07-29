export const validateNoteTitle = (
  currentTitle: string,
  newTitle: string | null | undefined
) => {
  if (!newTitle || newTitle.trim() === "") return "Title cannot be empty";

  if (currentTitle === newTitle) return;

  if (newTitle.length < 3) return "Title must be at least 3 character long";

  if (newTitle.length > 255) return "Title must be less than 255 characters";
};
