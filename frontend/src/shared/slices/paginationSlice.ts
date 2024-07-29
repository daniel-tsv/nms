import { PayloadAction, createSlice } from "@reduxjs/toolkit";
import {
  Direction,
  SortBy,
  type PaginationOptions,
} from "../../features/search/types";

interface PaginationState {
  pagination: PaginationOptions;
}

const initialState: PaginationState = {
  pagination: {
    page: 0,
    size: 8,
    direction: Direction.ASC,
    sortBy: SortBy.UPDATED_AT,
  },
};

const paginationSlice = createSlice({
  name: "pagination",
  initialState,
  reducers: {
    setPagination: (state, action: PayloadAction<PaginationOptions>) => {
      state.pagination = action.payload;
    },
  },
});

export const { setPagination } = paginationSlice.actions;
export default paginationSlice.reducer;
