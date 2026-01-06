import { useEffect, useState } from "react";
import cartApi from "../apis/cartApi";
import orderApi from "../apis/orderApi";
import { useNavigate } from "react-router-dom";

function Cart() {
  const [items, setItems] = useState([]);
  const [cartTotal, setCartTotal] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/login");
      return;
    }
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      const res = await cartApi.get("/cart");
      setItems(res.data.cartItems);
      setCartTotal(res.data.cartTotal);
    } catch (err) {
      console.error("Error fetching cart items", err);
    }
  };

  const updateQuantity = async (item, newQuantity) => {
    try {
      setItems((prev) =>
        prev
          .map((i) =>
            i.id === item.id ? { ...i, quantity: newQuantity } : i
          )
          .filter((i) => i.quantity > 0)
      );

      await cartApi.put("/cart/items", {
        id: item.id,
        quantity: newQuantity,
      });
      
      fetchCart();

    } catch (err) {
      console.error("Error updating quantity", err);
      fetchCart(); // fallback if something goes wrong
    }
  };

  const checkout = async () => {
    try {
      await orderApi.post("/orders");
      navigate("/orders");
    } catch (err) {
      console.error("Checkout failed", err);
      alert("Unable to place order");
    }
  };


  return (
    <div style={styles.container}>
      <h2>Your Cart</h2>

      {items.length === 0 && <p>Cart is empty</p>}

      {items.map((item) => (
        <div key={item.id} style={styles.row}>
          <img
            src={`${import.meta.env.VITE_API_BOOKS_URL}${item.imageUrl}`}
            alt={item.title}
            style={styles.image}
          />

          <div style={styles.details}>
            <h4>{item.title}</h4>
            <p>₹{item.price}</p>

            <div style={styles.quantityBox}>
              <button
                style={styles.qtyBtn}
                onClick={() => updateQuantity(item, item.quantity - 1)}
              >
                −
              </button>

              <span style={styles.qty}>{item.quantity}</span>

              <button
                style={styles.qtyBtn}
                onClick={() => updateQuantity(item, item.quantity + 1)}
              >
                +
              </button>
            </div>
          </div>
        </div>
      ))}

      {items.length > 0 && (
        <div style={styles.totalBox}>
          <h3>Total: ₹{cartTotal}</h3>
          <button style={styles.checkoutBtn} onClick={checkout}>Checkout</button>
        </div>
      )}

    </div>
  );
}

const styles = {
  container: {
    padding: "30px",
  },
  row: {
    display: "flex",
    alignItems: "center",
    gap: "20px",
    borderBottom: "1px solid #ddd",
    padding: "15px 0",
  },
  image: {
    width: "80px",
    height: "100px",
    objectFit: "cover",
    borderRadius: "6px",
  },
  details: {
    flex: 1,
  },
  quantityBox: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    marginTop: "8px",
  },
  qtyBtn: {
    width: "30px",
    height: "30px",
    fontSize: "18px",
    cursor: "pointer",
  },
  qty: {
    minWidth: "20px",
    textAlign: "center",
    fontWeight: "bold",
  },
  totalBox: {
    marginTop: "20px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  checkoutBtn: {
    padding: "10px 20px",
    fontSize: "16px",
    cursor: "pointer",
  },
};

export default Cart;
