import { api as API } from "../../../shared/util/api";
import { UserDTO } from "../types";

export const UserService = {
  async fetchUser() {
    const response = await API.get<UserDTO>("/user");
    return response.data;
  },

  async updateUser(userDTO: UserDTO) {
    const response = await API.patch("/user", userDTO);
    return response.data;
  },

  async deleteUser() {
    const response = await API.delete("/user");
    return response.data;
  },
};
