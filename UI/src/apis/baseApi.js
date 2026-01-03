import axios from "axios";

const baseApi = axios.create({
  baseURL: import.meta.env.VITE_API_AUTH_URL,
});

export default baseApi;
