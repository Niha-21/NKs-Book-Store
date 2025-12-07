import { useEffect, useState } from "react";
import booksApi from "../apis/booksApi";

function Books() {
  const [books, setBooks] = useState([]);

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

  return (
    <div style={styles.container}>
      <h2>Book Store</h2>

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
            <button style={styles.button}>Add to Cart</button>
          </div>
        ))}
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: "30px",
    textAlign: "center",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
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
    backgroundColor: "#0070f3",
    color: "#fff",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
  },
};

export default Books;
