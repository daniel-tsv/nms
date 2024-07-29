import type { UserDTO } from "../../user/types";

export interface AuthResponseDTO {
  userDTO: UserDTO;
  jwtToken: string;
}

export interface LoginDTO {
  username: string;
  password: string;
}

export interface RegisterDTO {
  username: string;
  password: string;
  email: string;
}
