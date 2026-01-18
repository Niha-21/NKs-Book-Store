import { useEffect, useState } from "react";
import orderApi from "../apis/orderApi";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";

function Orders() {
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/login");
      return;
    }
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const res = await orderApi.get("/orders");
      setOrders(res.data);
    } catch (err) {
      console.error("Error fetching orders", err);
    }
  };

  return (
    <>
    <Header />
    <div style={styles.container}>
      <h2 style={styles.headline}>Orders</h2>

      {orders.length === 0 && <p>No orders placed yet</p>}

      {orders.map((order, index) => (
        <div
            key={order.orderId}
            style={{ ...styles.orderCard, cursor: "pointer" }} 
            onClick={() => navigate(`/orders/${order.orderId}`)} 
        >
            <div style={styles.orderHeader}>
            <span><b>Order #{index + 1}</b></span>
            <span>Status: {order.status}</span>
            </div>

            <div>
            {order.items.map((item, idx) => (
                <div key={idx} style={styles.itemRow}>
                <span>{item.bookName}</span>
                <span>
                    ₹{item.price} × {item.quantity}
                </span>
                </div>
            ))}
            </div>

            <div style={styles.orderFooter}>
            <span>
                Total: <b>₹{order.totalAmount}</b>
            </span>
            <span style={styles.date}>
                {new Date(order.createdAt).toLocaleDateString()}
            </span>
            </div>
        </div>
        ))}
    </div>
    </>
  );
}

const styles = {
  container: {
    padding: "30px",
  },
  headline: {
    color: "#17213a",
    fontWeight: "bold",
    fontSize: "25px",
    // fontStyle: "italic",
    padding: "20px 0",
  },
  orderCard: {
    border: "1px solid #ddd",
    borderRadius: "8px",
    padding: "15px",
    marginBottom: "20px",
  },
  orderHeader: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "10px",
  },
  itemRow: {
    display: "flex",
    justifyContent: "space-between",
    padding: "4px 0",
  },
  orderFooter: {
    display: "flex",
    justifyContent: "space-between",
    marginTop: "10px",
    fontWeight: "bold",
  },
  date: {
    fontSize: "14px",
    color: "#555",
  },
};

export default Orders;
