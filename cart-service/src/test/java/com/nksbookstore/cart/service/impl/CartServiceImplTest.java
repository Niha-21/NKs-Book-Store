package com.nksbookstore.cart.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nksbookstore.cart.client.BookClient;
import com.nksbookstore.cart.entity.Cart;
import com.nksbookstore.cart.entity.CartItem;
import com.nksbookstore.cart.exception.*;
import com.nksbookstore.cart.model.*;
import com.nksbookstore.cart.repository.CartItemRepository;
import com.nksbookstore.cart.repository.CartRepository;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookClient bookClient;

    @InjectMocks
    private CartServiceImpl cartService;

    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUpSecurityContext() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(USER_ID.toString(), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addToCart_shouldCreateCartAndAddItem() {
        Cart cart = new Cart();
        cart.setUserId(USER_ID);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartItemDTO request = new CartItemDTO(null, null, 10L, null, null, null, 2);

        cartService.addToCart(request);

        assertEquals(1, cart.getCartItems().size());
        CartItem item = cart.getCartItems().get(0);
        assertEquals(10L, item.getBookId());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void addToCart_shouldUpdateQuantityIfItemExists() {
        CartItem item = new CartItem();
        item.setBookId(10L);
        item.setQuantity(1);

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        CartItemDTO request = new CartItemDTO(null, null, 10L, null, null, null, 5);

        cartService.addToCart(request);

        assertEquals(5, item.getQuantity());
        assertEquals(1, cart.getCartItems().size());
    }

    @Test
    void updateCartItemQuantity_shouldUpdateQuantity() {
        CartItem item = new CartItem();
        item.setId(1L);
        item.setQuantity(1);

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        CartItemDTO request = new CartItemDTO(1L, null, null, null, null, null, 3);

        cartService.updateCartItemQuantity(request);

        assertEquals(3, item.getQuantity());
    }

    @Test
    void updateCartItemQuantity_shouldRemoveItemIfQuantityZero() {
        CartItem item = new CartItem();
        item.setId(1L);

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        CartItemDTO request = new CartItemDTO(1L, null, null, null, null, null, 0);

        cartService.updateCartItemQuantity(request);

        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void updateCartItemQuantity_shouldThrowIfItemNotFound() {
        Cart cart = new Cart();
        cart.setUserId(USER_ID);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        CartItemDTO request = new CartItemDTO(99L, null, null, null, null, null, 1);

        assertThrows(CartItemNotFoundException.class,
                () -> cartService.updateCartItemQuantity(request));
    }

    @Test
    void getCart_shouldReturnCartResponse() {
        CartItem item = new CartItem();
        item.setId(1L);
        item.setBookId(10L);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);
        item.setCart(cart);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        BookDTO book = new BookDTO(10L, "Book", "img", BigDecimal.valueOf(100));
        when(bookClient.getBookById(10L)).thenReturn(book);

        CartResponseDTO response = cartService.getCart();

        assertEquals(1, response.getCartItems().size());
        assertEquals(BigDecimal.valueOf(200), response.getCartTotal());
    }

    @Test
    void getCart_shouldThrowBookNotFoundException() {
        CartItem item = new CartItem();
        item.setBookId(10L);

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(bookClient.getBookById(10L)).thenThrow(mock(FeignException.NotFound.class));

        assertThrows(BookNotFoundException.class, () -> cartService.getCart());
    }

    @Test
    void getCart_shouldThrowUnauthorizedException() {
        CartItem item = new CartItem();
        item.setBookId(10L);

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(bookClient.getBookById(10L))
                .thenThrow(mock(FeignException.Unauthorized.class));

        assertThrows(UnauthorizedException.class, () -> cartService.getCart());
    }

    @Test
    void removeCartItem_shouldRemoveItem() {
        CartItem item = new CartItem();
        item.setId(1L);

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        cartService.removeCartItem(1L);

        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void removeCartItem_shouldThrowIfNotFound() {
        Cart cart = new Cart();
        cart.setUserId(USER_ID);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        assertThrows(CartItemNotFoundException.class,
                () -> cartService.removeCartItem(1L));
    }

    @Test
    void clearCart_shouldClearItems() {
        CartItem item = new CartItem();

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.getCartItems().add(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        cartService.clearCart();

        assertTrue(cart.getCartItems().isEmpty());
    }
}
