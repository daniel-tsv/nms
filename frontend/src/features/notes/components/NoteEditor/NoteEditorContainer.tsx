import { useEffect, useState } from "react";
import { handleError } from "../../../../shared/util/api";
import { useAppDispatch, useAppSelector } from "../../../../store/hooks";
import { NoteService } from "../../services/NoteService";
import { setSelectedNote } from "../../slices/notesSlice";
import type { NoteDetailDTO } from "../../types";
import { NoteEditor } from "./NoteEditor";
import { NoteEditorHeader } from "./NoteEditorHeader/NoteEditorHeader";

export const NoteEditorContainer = ({
  headerChildren,
}: {
  headerChildren: React.ReactNode;
}) => {
  const dispatch = useAppDispatch();

  const selectedNote = useAppSelector((state) => state.notes.selectedNote);
  const [previousNoteContents, setPreviousNoteContents] = useState("");

  useEffect(() => {
    if (selectedNote?.contents) setPreviousNoteContents(selectedNote.contents);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedNote?.createdAt]);

  const prepareNoteForUpdate = (note: NoteDetailDTO) => {
    return {
      ...note,
      updatedAt: note.updatedAt
        ? new Date(note.updatedAt).toISOString()
        : undefined,
      createdAt: note.createdAt
        ? new Date(note.createdAt).toISOString()
        : undefined,
    };
  };
  const handleSaveNoteContents = async (note: NoteDetailDTO) => {
    if (note.contents === previousNoteContents) {
      return;
    }

    try {
      const updatedNote: NoteDetailDTO = await NoteService.updateNote(
        note.title,
        prepareNoteForUpdate(note)
      );

      dispatch(setSelectedNote(updatedNote));
      setPreviousNoteContents(updatedNote.contents);
    } catch (error) {
      handleError(error);
    }
  };

  const handleContentChange = (newContent: string) => {
    if (selectedNote)
      dispatch(
        setSelectedNote({
          ...selectedNote,
          updatedAt: String(new Date()),
          contents: newContent,
        })
      );
  };

  return (
    <NoteEditor
      selectedNote={selectedNote}
      handleContentChange={handleContentChange}
    >
      <NoteEditorHeader
        handleSaveNoteContents={handleSaveNoteContents}
        selectedNote={selectedNote}
        contentsHasChanged={previousNoteContents !== selectedNote?.contents}
      >
        {headerChildren}
      </NoteEditorHeader>
    </NoteEditor>
  );
};
