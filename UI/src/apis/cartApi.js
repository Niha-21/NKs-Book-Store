import axios from "axios";
import { applyAuthInterceptors } from "./apiClient";

const cartApi = axios.create({
  baseURL: import.meta.env.VITE_API_CART_URL,
});

applyAuthInterceptors(cartApi);

export default cartApi;
