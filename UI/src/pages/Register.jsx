import { useState } from "react";
import api from "../apis/baseApi";
import { useNavigate } from "react-router-dom";

    function Register() {
        const [username, setUsername] = useState("");
        const [email, setEmail] = useState("");
        const [password, setPassword] = useState("");
        const [message, setMessage] = useState("");
        const navigate = useNavigate();
        
        const handleRegister = async (e) => {
        e.preventDefault();

        try {
            const response = await api.post("/auth/register", {
                username,
                password,
                email
            });

            if (response.status === 200) {
                navigate("/login");
            }

            setMessage(response.data);
            setUsername("");
            setPassword("");
            setEmail("");

        } catch (error) {
            if (error.response && error.response.status === 409) {
                setMessage("Username already exists. Please enter another.");
            } else {
                setMessage("Something went wrong. Please try again.");
            }
        }
        };

        return (
            <div style={styles.container}>
            <h2>Register</h2>

            <form onSubmit={handleRegister} style={styles.form}>
                <input
                type="text"
                placeholder="Enter username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                style={styles.input}
                />

                <input
                type="password"
                placeholder="Enter password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={styles.input}
                />

                <input
                type="email"
                placeholder="Enter email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                style={styles.input}
                />

                <button type="submit" style={styles.button}>
                Register
                </button>
            </form>

            {message && <p style={{ marginTop: "20px" }}>{message}</p>}
            
            </div>
        );
    }

    const styles = {
        container: {
            maxWidth: "350px",
            margin: "50px auto",
            textAlign: "center",
            padding: "20px",
            border: "1px solid #ddd",
            borderRadius: "8px",
        },
        form: {
            display: "flex",
            flexDirection: "column",
            gap: "15px",
        },
        input: {
            padding: "10px",
            fontSize: "16px",
        },
        button: {
            padding: "10px",
            backgroundColor: "#0070f3",
            color: "#fff",
            cursor: "pointer",
            border: "none",
            borderRadius: "5px",
        },
    };

export default Register;
