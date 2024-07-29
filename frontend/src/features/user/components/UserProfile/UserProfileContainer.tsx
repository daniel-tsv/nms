import axios from "axios";
import { useEffect } from "react";
import type { ErrorResponseDTO } from "../../../../shared/types";
import {
  handleBackendError,
  handleError,
  handleUnexpectedError,
} from "../../../../shared/util/api";
import { useAppDispatch, useAppSelector } from "../../../../store/hooks";
import { removeToken } from "../../../auth/util/auth";
import { UserService } from "../../services/UserService";
import { clearUserState, setUser } from "../../slices/userSlice";
import type { UserDTO } from "../../types";
import { UserProfile } from "./UserProfile";

export const UserProfileContainer = () => {
  const dispatch = useAppDispatch();
  const user: UserDTO | null = useAppSelector((state) => state.user.user);

  const fetchUser = async () => {
    try {
      const user: UserDTO = await UserService.fetchUser();
      dispatch(setUser(user));
    } catch (error) {
      if (axios.isAxiosError(error) && error.response) {
        const response: ErrorResponseDTO = error.response.data;
        if (response.status === "NOT_FOUND") handleLogout();
        handleBackendError(error);
      }
      handleUnexpectedError(error);
    }
  };

  useEffect(() => {
    fetchUser();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleSaveUserProfile = async (
    newUsername: string,
    newEmail: string
  ) => {
    try {
      await UserService.updateUser({ username: newUsername, email: newEmail });
      dispatch(
        setUser({
          username: newUsername,
          email: newEmail,
          numberOfNotes: user?.numberOfNotes,
        })
      );
    } catch (error) {
      handleError(error);
    }
  };

  const handleDeleteUserProfile = async () => {
    if (window.confirm("Are you sure you want to delete your account?")) {
      await UserService.deleteUser()
        .then(() => handleLogout())
        .catch((err) => handleError(err));
    }
  };

  const handleLogout = () => {
    removeToken();
    dispatch(clearUserState());
    window.location.reload();
  };

  return (
    user && (
      <UserProfile
        username={user.username}
        email={user.email}
        notesCount={user.numberOfNotes ?? 0}
        handleSave={handleSaveUserProfile}
        handleDelete={handleDeleteUserProfile}
        handleLogout={handleLogout}
      />
    )
  );
};
