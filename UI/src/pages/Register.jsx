import { useState } from "react";
import api from "../apis/baseApi";
import { useNavigate, Link } from "react-router-dom";
import Header from "../components/Header";

function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const response = await api.post("/auth/register", {
        username,
        password,
        email,
      });

      if (response.status === 200) {
        navigate("/login");
      }
      
      setMessage(response.data);
      setUsername("");
      setPassword("");
      setEmail("");

    } catch (error) {
      if (error.response?.status === 409) {
        setMessage("Username already exists. Please try another.");
      } else {
        setMessage("Something went wrong. Please try again.");
      }
    }
  };

  return (
    <>
      <Header />

      <div style={styles.page}>
        <div style={styles.card}>
          <h2 style={styles.headline}>Register</h2>

          {message && <p style={styles.message}>{message}</p>}

          <form onSubmit={handleRegister}>
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
              <label>Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
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
              Register
            </button>
          </form>

          <p style={styles.footerText}>
            Already have an account? <Link to="/login">Login</Link>
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
    width: "340px",
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
    color: "#2c4783",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
  message: {
    textAlign: "center",
    color: "red",
    marginBottom: "10px",
    fontSize: "14px",
  },
  footerText: {
    marginTop: "15px",
    textAlign: "center",
    fontSize: "14px",
  },
};

export default Register;