import { useEffect, useState } from "react";
import cartApi from "../apis/cartApi";
import booksApi from "../apis/booksApi";
import { useNavigate } from "react-router-dom";

function Cart() {
  const [items, setItems] = useState([]);
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
      const res = await cartApi.get("/cart/items");

      const enrichedItems = await Promise.all(
        res.data.map(async (item) => {
          const bookRes = await booksApi.get(`/books/${item.bookId}`);
          return {
            ...item,
            book: bookRes.data,
          };
        })
      );

      setItems(enrichedItems);
    } catch (err) {
      console.error("Error fetching cart items", err);
    }
  };

  const updateQuantity = async (item, newQuantity) => {
    try {
      // optimistic UI update
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
    } catch (err) {
      console.error("Error updating quantity", err);
      fetchCart(); // fallback if something goes wrong
    }
  };

  return (
    <div style={styles.container}>
      <h2>Your Cart</h2>

      {items.length === 0 && <p>Cart is empty</p>}

      {items.map((item) => (
        <div key={item.id} style={styles.row}>
          <img
            src={`${import.meta.env.VITE_API_BOOKS_URL}${item.book.imageUrl}`}
            alt={item.book.title}
            style={styles.image}
          />

          <div style={styles.details}>
            <h4>{item.book.title}</h4>

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
};

export default Cart;
