import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from './App.jsx'
import Login from './pages/Login.jsx';
import Register from "./pages/Register";
import Books from "./pages/Books";
import Cart from "./pages/Cart";
import Orders from "./pages/Orders";
import OrderDetails from "./pages/OrderDetails";
import Profile from "./pages/Profile";
import PrivateRoute from "./components/PrivateRoute";

ReactDOM.createRoot(document.getElementById('root')).render(
  <BrowserRouter>
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/home" element={<Books />} />
      <Route path="/cart" element={<PrivateRoute>
                                    <Cart />
                                    </PrivateRoute>} />
      <Route path="/profile" element={<PrivateRoute>
                                      <Profile />
                                      </PrivateRoute>} />
      <Route path="/orders" element={<PrivateRoute>
                                      <Orders />
                                      </PrivateRoute>} />
      <Route path="/orders/:orderId" element={<PrivateRoute>
                                              <OrderDetails />
                                              </PrivateRoute>} />
    </Routes>
  </BrowserRouter>
)
