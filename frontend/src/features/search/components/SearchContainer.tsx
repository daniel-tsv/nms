import { useState } from "react";
import { setPagination } from "../../../shared/slices/paginationSlice";
import { handleError } from "../../../shared/util/api";
import { useAppDispatch, useAppSelector } from "../../../store/hooks";
import { NoteService } from "../../notes/services/NoteService";
import {
  clearSelectedNote,
  setNoteSummaries,
} from "../../notes/slices/notesSlice";
import type { NoteSummaryDTO } from "../../notes/types";
import { PaginationOptions, SearchType } from "../types";
import { SearchBar } from "./SearchBar/SearchBar";
import { SearchOptions } from "./SearchOptions/SearchOptions";

export const SearchContainer = () => {
  const dispatch = useAppDispatch();
  const [searchOptionsClosed, setSearchOptionsClosed] = useState(true);
  const [searchValue, setSearchValue] = useState("");
  const [searchType, setSearchType] = useState(SearchType.TITLE);
  const { pagination } = useAppSelector((state) => state.pagination);
  const { selectedNote } = useAppSelector((state) => state.notes);

  const handleSearch = async () => {
    try {
      let notes: NoteSummaryDTO[];

      if (searchValue.trim() === "")
        notes = (await NoteService.getNotes(pagination)).notes;
      else {
        notes = (
          await NoteService.searchNotes(searchValue, searchType, pagination)
        ).notes;
      }

      dispatch(setNoteSummaries(notes));
      if (!notes.find((note) => note.title === selectedNote?.title)) {
        dispatch(clearSelectedNote());
      }
    } catch (error) {
      handleError(error);
    }
  };

  const handleSetSearchType = (type: SearchType) => {
    setSearchType(type);
    if (searchValue.trim() !== "") handleSearch();
  };

  return (
    <>
      <SearchBar
        handleSearch={handleSearch}
        handleToggleOptions={() => setSearchOptionsClosed((prev) => !prev)}
        searchValue={searchValue}
        setSearchValue={(value: string) => setSearchValue(value)}
      />
      <SearchOptions
        searchOptionsClosed={searchOptionsClosed}
        searchType={searchType}
        setSearchType={(type: SearchType) => handleSetSearchType(type)}
        pagination={pagination}
        setPagination={(pag: PaginationOptions) => dispatch(setPagination(pag))}
      />
    </>
  );
};
