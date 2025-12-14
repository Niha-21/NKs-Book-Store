import axios from "axios";

const cartApi = axios.create({
  baseURL: import.meta.env.VITE_API_CART_URL,
});

export default cartApi;
