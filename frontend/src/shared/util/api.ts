import axios from "axios";
import { getToken } from "../../features/auth/util/auth";
import { ErrorResponseDTO } from "../types";

const baseURL = import.meta.env.VITE_API_URL || "http://localhost:8081/api";

export const api = axios.create({
  baseURL,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const jwtToken = getToken();
    if (jwtToken) config.headers.Authorization = `Bearer ${jwtToken}`;
    return config;
  },
  (error: Error) => Promise.reject(error)
);

export const handleUnexpectedError = (error: unknown) => {
  if (!axios.isAxiosError(error) || !error.response) {
    console.error(error);
    alert("An unexpected error occurred");
  }
};

export const handleBackendError = (error: unknown) => {
  if (axios.isAxiosError(error) && error.response) {
    const response: ErrorResponseDTO = error.response.data;
    console.error(response);
    alert(response.message);
  }
};

export const handleError = (error: unknown) => {
  handleUnexpectedError(error);
  handleBackendError(error);
};
