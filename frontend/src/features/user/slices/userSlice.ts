import { PayloadAction, createSlice } from "@reduxjs/toolkit";
import { UserDTO } from "../types";

interface UserState {
  user: UserDTO | null;
}

const initialState: UserState = {
  user: null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser(state, action: PayloadAction<UserDTO>) {
      state.user = action.payload;
    },
    clearUserState(state) {
      state.user = null;
    },
  },
});

export const { setUser, clearUserState } = userSlice.actions;
export default userSlice.reducer;
