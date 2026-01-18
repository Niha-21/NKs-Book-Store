import React from "react";
import { useNavigate } from "react-router-dom";
import useLogout from "../hooks/useLogout"; 
import { FaUserCircle, FaShoppingCart, FaSignInAlt, FaSignOutAlt } from "react-icons/fa";
import { GiSpellBook } from "react-icons/gi";

function Header({ cartCount = 0 }) {
  const navigate = useNavigate();
  const logout = useLogout();

  const isLoggedIn = !!localStorage.getItem("token");

  return (
    <header style={styles.header}>
      <h1 style={styles.logo} onClick={() => navigate("/home")}>
        <GiSpellBook size={30} style={{ marginRight: "10px", color: "#9df43f" }} />
          NKS Book Store
      </h1>

      <nav style={styles.nav}>
        {isLoggedIn && (
            <>
            <div style={styles.iconContainer} onClick={() => navigate("/cart")}>
            <FaShoppingCart size={24} />
            <span style={styles.iconText}>Cart</span>
            </div>

            <div style={styles.iconContainer} onClick={() => navigate("/profile")}>
            <FaUserCircle size={24} />
            <span style={styles.iconText}>Profile</span>
            </div>

            <div style={styles.iconContainer} onClick={logout} title="Logout">
                <FaSignOutAlt size={24} />
            <span style={styles.iconText}>Logout</span>
            </div>
            </>
        )}
        {!isLoggedIn && (
          <div style={styles.iconContainer} onClick={() => navigate("/login")} title="Login">
            <FaSignInAlt size={24} />
            <span style={styles.iconText}>Login</span>
          </div>
        )}
      </nav>
    </header>
  );
}

const styles = {
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "10px 30px",
    borderBottom: "1px solid #ddd",
    backgroundColor: "var(--header-bg, #17213a)",
  },
  logo: {
    cursor: "pointer",
    color: "var(--primary-color, #9df43f)",
    userSelect: "none",
    fontStyle: "italic",
    fontWeight: "600", 
    letterSpacing: "0.5px",
  },
  nav: {
    display: "flex",
    alignItems: "center",
    gap: "20px",
  },
  iconContainer: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    cursor: "pointer",
    color: "var(--primary-color, #9df43f)",
  },
  iconText: {
    fontSize: "12px",
    marginTop: "4px",
  },
};

export default Header;
