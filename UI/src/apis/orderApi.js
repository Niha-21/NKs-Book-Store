import axios from "axios";
import { applyAuthInterceptors } from "./apiClient";

const orderApi = axios.create({
  baseURL: import.meta.env.VITE_API_ORDERS_URL,
});

applyAuthInterceptors(orderApi);

export default orderApi;
