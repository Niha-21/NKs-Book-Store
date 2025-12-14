import axios from "axios";

const cartApi = axios.create({
  baseURL: import.meta.env.VITE_API_CART_URL,
});


cartApi.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});


export default cartApi;
