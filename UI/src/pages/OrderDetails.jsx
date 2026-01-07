import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import orderApi from "../apis/orderApi";

function OrderDetails() {
  const { orderId } = useParams();
  const navigate = useNavigate();

  const [order, setOrder] = useState(null);

  useEffect(() => {
    fetchOrder();
  }, []);

  const fetchOrder = async () => {
    try {
      const res = await orderApi.get(`/orders/${orderId}`);
      setOrder(res.data);
    } catch (err) {
      console.error("Failed to fetch order", err);
    }
  };

  if (!order) return <p>Loading order...</p>;

  return (
    <div style={styles.container}>
      <button onClick={() => navigate("/orders")} style={styles.backBtn}>
        ← Back to Orders
      </button>

      <h2>Order #{order.orderId}</h2>

      <div style={styles.meta}>
        <span>
          <b>Date:</b>{" "}
          {new Date(order.createdAt).toLocaleString()}
        </span>
        <span>
          <b>Status:</b> {order.status}
        </span>
      </div>

      <div style={styles.items}>
        {order.items.map((item, index) => (
          <div key={index} style={styles.row}>
            <img
              src={`${import.meta.env.VITE_API_BOOKS_URL}${item.imageUrl}`}
              alt={item.bookName}
              style={styles.image}
            />

            <div style={styles.details}>
              <h4>{item.bookName}</h4>
              <p>
                ₹{item.price} × {item.quantity}
              </p>
            </div>

            <div style={styles.subtotal}>
              ₹{item.price * item.quantity}
            </div>
          </div>
        ))}
      </div>

      <div style={styles.total}>
        <h3>Total: ₹{order.totalAmount}</h3>
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: "30px",
    maxWidth: "900px",
    margin: "0 auto",
  },
  backBtn: {
    marginBottom: "20px",
    cursor: "pointer",
  },
  meta: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "20px",
    color: "#555",
  },
  items: {
    borderTop: "1px solid #ddd",
    marginTop: "20px",
  },
  row: {
    display: "flex",
    alignItems: "center",
    gap: "20px",
    padding: "15px 0",
    borderBottom: "1px solid #eee",
  },
  image: {
    width: "70px",
    height: "90px",
    objectFit: "cover",
    borderRadius: "6px",
  },
  details: {
    flex: 1,
  },
  subtotal: {
    fontWeight: "bold",
  },
  total: {
    textAlign: "right",
    marginTop: "20px",
  },
};

export default OrderDetails;
