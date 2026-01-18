import Header from "../components/Header";
import { useNavigate } from "react-router-dom";

function Profile() {
  const navigate = useNavigate();

  const username = localStorage.getItem("username"); 

  return (
    <>
      <Header />

      <div style={styles.container}>
        <div style={styles.card}>
          <h2 style={styles.headline}>My Profile</h2>

          <div style={styles.section}>
            <p><b>Username:</b> {username || "User"}</p>
          </div>

          <div style={styles.actions}>
            <button
              style={styles.button}
              onClick={() => navigate("/orders")}
            >
              View My Orders
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

const styles = {
  container: {
    minHeight: "calc(100vh - 60px)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#fafafa",
  },  
  headline: {
    color: "#17213a",
    fontWeight: "bold",
    textAlign: "center",
    // fontStyle: "italic",
  },
  card: {
    width: "360px",
    padding: "24px",
    borderRadius: "10px",
    backgroundColor: "#fff",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    transform: "translateY(-180px)",
  },
  section: {
    marginBottom: "20px",
  },
  actions: {
    display: "flex",
    justifyContent: "left",
  },
  button: {
    padding: "10px 20px",
    backgroundColor: "#9df43f",
    color: "#17213a",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
};

export default Profile;
