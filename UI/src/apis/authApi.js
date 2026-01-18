import axios from "axios";

const authApi = axios.create({
  baseURL: import.meta.env.VITE_API_AUTH_URL,
});

export default authApi;
