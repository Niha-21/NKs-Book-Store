import { useNavigate } from "react-router-dom";
import authApi from "../apis/authApi";

export default function useLogout() {
  const navigate = useNavigate();

  const logout = async () => {
    try {
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        await authApi.post("/auth/logout", { refreshToken });
      }
    } catch (error) {
      console.error("Logout failed", error);
    } finally {
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("username");
      navigate("/login");
    }
  };

  return logout;
}
