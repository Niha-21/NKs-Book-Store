import { useEffect, useState } from "react";
import booksApi from "../apis/booksApi";
import cartApi from "../apis/cartApi";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";

function Books() {
  const [books, setBooks] = useState([]);
  const [addingBookId, setAddingBookId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await booksApi.get("/books/listBooks");
      setBooks(response.data);
    } catch (error) {
      console.log("Error fetching books", error);
    }
  };

  const addToCart = async (bookId) => {
    try {

      setAddingBookId(bookId);

      await cartApi.post("/cart/items", {
        bookId: bookId,
        quantity: 1,
      });

      alert("Item added to cart!");

    } catch (error) {
      if (error.response.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
      }
      console.log("Error adding to cart", error);
      alert("Please login to add items to cart");
    } finally {
      setAddingBookId(null);
    }
  };

  return (
    <>
      <Header cartCount={0} />
      <div style={styles.container}>
        <div style={styles.grid}>
          {books.map((book) => (
            <div key={book.id} style={styles.card}>
              <img
                src={`${import.meta.env.VITE_API_BOOKS_URL}${book.imageUrl}`}
                alt={book.title}
                style={styles.image}
              />
              <h3>{book.title}</h3>
              <p>{book.description}</p>
              <p style={styles.price}>₹{book.price}</p>
              <button
                style={styles.button}
                disabled={addingBookId === book.id}
                onClick={() => addToCart(book.id)}
              >
                {addingBookId === book.id ? "Adding..." : "Add to Cart"}
              </button>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

const styles = {
  container: {
    textAlign: "center",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(230px, 1fr))",
    gap: "20px",
    marginTop: "20px",
  },
  card: {
    border: "1px solid #ddd",
    borderRadius: "10px",
    padding: "15px",
    textAlign: "left",
  },
  image: {
    width: "100%",
    height: "180px",
    objectFit: "cover",
    borderRadius: "8px",
  },
  price: {
    fontWeight: "bold",
    marginTop: "10px",
  },
  button: {
    marginTop: "10px",
    padding: "10px",
    width: "100%",
    backgroundColor: "#9df43f",
    color: "#17213a",
    borderRadius: "5px",
    cursor: "pointer",
  },
};

export default Books;
