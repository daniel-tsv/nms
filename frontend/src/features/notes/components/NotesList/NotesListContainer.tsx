import { useEffect, useState } from "react";
import { PageSelector } from "../../../../shared/components/PageSelector/PageSelector";
import { setPagination } from "../../../../shared/slices/paginationSlice";
import { handleError } from "../../../../shared/util/api";
import { useAppDispatch, useAppSelector } from "../../../../store/hooks";
import { setUser } from "../../../user/slices/userSlice";
import { NoteService } from "../../services/NoteService";
import {
  clearSelectedNote,
  removeNoteSummary,
  setEditingTitle,
  setNoteSummaries,
  setNoteSummaryTitle,
  setSelectedNote,
  setSelectedNoteTitle,
} from "../../slices/notesSlice";
import type { NoteDetailDTO, NotesPageDTO, NoteSummaryDTO } from "../../types";
import { validateNoteTitle } from "../../util/noteValidator";
import { CreateNewNoteButton } from "./CreateNewNoteButton/CreateNewNoteButton";
import { NotesList } from "./NotesList";

export const NotesListContainer = () => {
  const dispatch = useAppDispatch();

  const { notes, isEditingTitle, selectedNote } = useAppSelector(
    (state) => state.notes
  );
  const { pagination } = useAppSelector((state) => state.pagination);
  const { user } = useAppSelector((state) => state.user);
  const [previousNoteTitle, setPreviousNoteTitle] = useState("");
  const [totalPages, setTotalPages] = useState(0);
  const [isUpdating, setIsUpdating] = useState(false);

  const refreshNotes = async () => {
    if (isUpdating) return;
    setIsUpdating(true);
    try {
      const notesPage: NotesPageDTO = await NoteService.getNotes(pagination);
      const notes: NoteSummaryDTO[] = notesPage.notes;
      setTotalPages(notesPage.totalPages);
      dispatch(setNoteSummaries(notes));
    } catch (error) {
      handleError(error);
    } finally {
      setIsUpdating(false);
    }
  };

  useEffect(() => {
    refreshNotes();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pagination]);

  const handleSaveNoteSummaryTitle = async (
    title: string,
    newTitle: string
  ) => {
    if (title === newTitle) {
      dispatch(setEditingTitle(false));
      return;
    }
    const error = validateNoteTitle(title, newTitle);
    if (error) {
      alert(error);
      return;
    }
    try {
      await NoteService.updateNote(title, { title: newTitle });
      dispatch(setNoteSummaryTitle({ title, newTitle }));
      dispatch(setEditingTitle(false));
    } catch (error) {
      handleError(error);
    }
  };

  const handleCreateNote = async () => {
    if (isEditingTitle) resetEditing();
    try {
      const newNote: NoteDetailDTO = {
        title: `New note ${Date.now().toString().substring(9)}`,
        contents: "",
      };
      await NoteService.createNote(newNote);
      await refreshNotes();
      handleSelectNote(newNote.title);
      handleEditTitle(newNote.title);
      if (user?.numberOfNotes != undefined) {
        dispatch(setUser({ ...user, numberOfNotes: user?.numberOfNotes + 1 }));
      }
      refreshNotes();
    } catch (error) {
      handleError(error);
    }
  };

  const handleSetTitle = (title: string, newTitle: string) => {
    if (title === newTitle) return;
    dispatch(setSelectedNoteTitle(newTitle));
    dispatch(setNoteSummaryTitle({ title, newTitle }));
  };

  const handleDeleteNote = async (title: string) => {
    if (isEditingTitle) resetEditing();
    try {
      await NoteService.deleteNote(title);
      dispatch(removeNoteSummary(title));
      dispatch(clearSelectedNote());
      if (user?.numberOfNotes)
        dispatch(setUser({ ...user, numberOfNotes: user?.numberOfNotes - 1 }));
      refreshNotes();
    } catch (error) {
      handleError(error);
    }
  };

  const handleSelectNote = async (title: string) => {
    if (isEditingTitle) resetEditing();
    setPreviousNoteTitle(title);

    try {
      const note: NoteDetailDTO = await NoteService.getNoteByTitle(title);
      dispatch(setSelectedNote(note));
    } catch (error) {
      handleError(error);
    }
  };

  const handleEditTitle = (currentTitle: string) => {
    setPreviousNoteTitle(currentTitle);
    dispatch(setEditingTitle(true));
  };

  const resetEditing = () => {
    dispatch(setEditingTitle(false));
    if (selectedNote) {
      dispatch(
        setNoteSummaryTitle({
          title: selectedNote?.title ?? "",
          newTitle: previousNoteTitle,
        })
      );
      dispatch(setSelectedNoteTitle(previousNoteTitle));
    }
  };

  const handleSetPage = (page: number) => {
    dispatch(setPagination({ ...pagination, page }));
  };

  return (
    <>
      <CreateNewNoteButton handleCreateNote={handleCreateNote} />
      <NotesList
        isUpdating={isUpdating}
        notes={notes}
        isEditingTitle={isEditingTitle}
        selectedNote={selectedNote}
        previousNoteTitle={previousNoteTitle}
        handleSelectNote={handleSelectNote}
        handleSaveNoteSummaryTitle={handleSaveNoteSummaryTitle}
        handleEditTitle={handleEditTitle}
        handleSetTitle={handleSetTitle}
        handleDeleteNote={handleDeleteNote}
      />
      {totalPages > 1 && (
        <PageSelector
          totalPages={totalPages}
          page={pagination.page}
          setPage={handleSetPage}
        />
      )}
    </>
  );
};
