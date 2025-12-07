import { useState } from "react";
import baseApi from "../apis/baseApi";
import { Link } from "react-router-dom";

function Login() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await baseApi.post("/auth/login", {
        username,
        password
      });

      // save token in localStorage
      localStorage.setItem("token", response.data.token);

      // go to /home
      window.location.href = "/home";

    } catch (err) {
      setError("Invalid username or password");
    }
  };

  return (
    <div style={{ width: "300px", margin: "100px auto" }}>
      <h2>Login</h2>

      {error && <p style={{color:"red"}}>{error}</p>}

      <form onSubmit={handleLogin}>
        <div>
          <label>Username</label><br />
          <input 
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>

        <div style={{marginTop:"10px"}}>
          <label>Password</label><br />
          <input 
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <button 
          type="submit" 
          style={{marginTop:"15px"}}
        >
          Login
        </button>
      </form>
      <p>
        Don’t have an account? <Link to="/register">Register here</Link>
      </p>
    </div>
  );
}

export default Login;
