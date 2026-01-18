import { useState } from "react";
import baseApi from "../apis/baseApi";
import { Link } from "react-router-dom";
import Header from "../components/Header";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      navigate("/home"); 
    }
  }, [navigate]);
  

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await baseApi.post("/auth/login", {
        username,
        password,
      });

      localStorage.setItem("token", response.data.token);
      localStorage.setItem("refreshToken", response.data.refreshToken);
      localStorage.setItem("username", username);
      window.location.href = "/home";
    } catch (err) {
      setError("Invalid username or password");
    }
  };

  return (
    <>
      <Header />

      <div style={styles.page}>
        <div style={styles.card}>
          <h2 style={styles.headline}>Login</h2>

          {error && <p style={styles.error}>{error}</p>}

          <form onSubmit={handleLogin}>
            <div style={styles.field}>
              <label>Username</label>
              <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                style={styles.input}
                required
              />
            </div>

            <div style={styles.field}>
              <label>Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={styles.input}
                required
              />
            </div>

            <button type="submit" style={styles.button}>
              Login
            </button>
          </form>

          <p style={styles.footerText}>
            Don’t have an account?{" "}
            <Link to="/register">Register here</Link>
          </p>
        </div>
      </div>
    </>
  );
}

const styles = {
  page: {
    minHeight: "calc(100vh - 60px)", 
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#fafafa",
  },
  card: {
    width: "320px",
    padding: "24px",
    borderRadius: "10px",
    backgroundColor: "#fff",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    transform: "translateY(-40px)",
  },
  headline: {
    color: "#17213a",
    fontWeight: "bold",
    textAlign: "center",
    // fontStyle: "italic",
  },
  field: {
    marginBottom: "12px",
  },
  input: {
    width: "95%",
    padding: "8px",
    marginTop: "4px",
    borderRadius: "5px",
    border: "1px solid #ccc",
  },
  button: {
    width: "100%",
    marginTop: "15px",
    padding: "10px",
    backgroundColor: "#9df43f",
    color: "#17213a",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
  error: {
    color: "red",
    textAlign: "center",
    marginBottom: "10px",
  },
  footerText: {
    marginTop: "15px",
    textAlign: "center",
    fontSize: "14px",
  },
};

export default Login;
