import { configureStore } from "@reduxjs/toolkit";
import notesSlice from "../features/notes/slices/notesSlice";
import userSlice from "../features/user/slices/userSlice";
import paginationSlice from "../shared/slices/paginationSlice";

export const store = configureStore({
  reducer: {
    user: userSlice,
    notes: notesSlice,
    pagination: paginationSlice,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
