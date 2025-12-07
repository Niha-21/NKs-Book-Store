import axios from "axios";

const booksApi = axios.create({
  baseURL: import.meta.env.VITE_API_BOOKS_URL,
});

export default booksApi;
