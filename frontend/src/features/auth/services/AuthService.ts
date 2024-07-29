import type { ErrorResponseDTO } from "../../../shared/types";
import { api } from "../../../shared/util/api";
import type { AuthResponseDTO, LoginDTO, RegisterDTO } from "../types";

export const AuthService = {
  login: async (
    loginData: LoginDTO
  ): Promise<AuthResponseDTO | ErrorResponseDTO> => {
    const response = await api.post<AuthResponseDTO | ErrorResponseDTO>(
      "/login",
      loginData
    );
    return response.data;
  },
  register: async (registerData: RegisterDTO): Promise<AuthResponseDTO> => {
    const response = await api.post<AuthResponseDTO>("/register", registerData);
    return response.data;
  },
};
