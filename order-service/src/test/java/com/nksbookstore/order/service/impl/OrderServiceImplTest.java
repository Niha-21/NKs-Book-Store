package com.nksbookstore.order.service.impl;

import com.nksbookstore.order.client.CartClient;
import com.nksbookstore.order.entity.Order;
import com.nksbookstore.order.entity.OrderItem;
import com.nksbookstore.order.entity.OrderStatus;
import com.nksbookstore.order.exception.CartEmptyException;
import com.nksbookstore.order.exception.CartServiceUnavailableException;
import com.nksbookstore.order.exception.OrderNotFoundException;
import com.nksbookstore.order.exception.UnauthorizedException;
import com.nksbookstore.order.model.CartItemDTO;
import com.nksbookstore.order.model.CartResponseDTO;
import com.nksbookstore.order.model.OrderResponseDTO;
import com.nksbookstore.order.repository.OrderRepository;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartClient cartClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null)
        );
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createOrder_success() {
        CartResponseDTO cartResponse = buildCartResponse();

        when(cartClient.getCart()).thenReturn(cartResponse);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> {
                    Order o = inv.getArgument(0);
                    o.setId(10L);
                    return o;
                });

        OrderResponseDTO response = orderService.createOrder();

        assertNotNull(response);
        assertEquals(10L, response.getOrderId());
        assertEquals(OrderStatus.CREATED, response.getStatus());
        assertEquals(1, response.getItems().size());

        verify(cartClient).getCart();
        verify(cartClient).clearCart();
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_cartEmpty_throwsException() {
        CartResponseDTO cartResponse = new CartResponseDTO();
        cartResponse.setCartItems(List.of());
        cartResponse.setCartTotal(BigDecimal.ZERO);

        when(cartClient.getCart()).thenReturn(cartResponse);

        assertThrows(CartEmptyException.class, () -> orderService.createOrder());

        verify(orderRepository, never()).save(any());
        verify(cartClient, never()).clearCart();
    }

    @Test
    void createOrder_cartNotFound_throwsCartEmptyException() {
        when(cartClient.getCart()).thenThrow(feignNotFound());

        assertThrows(CartEmptyException.class, () -> orderService.createOrder());
    }

    @Test
    void createOrder_unauthorizedCartAccess() {
        when(cartClient.getCart()).thenThrow(feignUnauthorized());

        assertThrows(UnauthorizedException.class, () -> orderService.createOrder());
    }

    @Test
    void createOrder_cartServiceDown() {
        when(cartClient.getCart()).thenThrow(feignServiceUnavailable());

        assertThrows(CartServiceUnavailableException.class, () -> orderService.createOrder());
    }

    @Test
    void createOrder_clearCartFails_orderStillCreated() {
        CartResponseDTO cartResponse = buildCartResponse();

        when(cartClient.getCart()).thenReturn(cartResponse);
        doThrow(feignServiceUnavailable())
        .when(cartClient)
        .clearCart();
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> {
                    Order o = inv.getArgument(0);
                    o.setId(20L);
                    return o;
                });

        OrderResponseDTO response = orderService.createOrder();

        assertEquals(20L, response.getOrderId());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrdersByUser_success() {
        Order order = buildOrder(1L);
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));

        List<OrderResponseDTO> orders = orderService.getOrdersByUser();

        assertEquals(1, orders.size());
        verify(orderRepository).findByUserId(1L);
    }

    @Test
    void getOrderById_success() {
        Order order = buildOrder(5L);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));

        OrderResponseDTO response = orderService.getOrderById(5L);

        assertEquals(5L, response.getOrderId());
    }

    @Test
    void getOrderById_notFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderById(99L));
    }

    @Test
    void createOrder_noAuthentication_throwsUnauthorized() {
        SecurityContextHolder.clearContext();

        assertThrows(UnauthorizedException.class,
                () -> orderService.createOrder());
    }

    private CartResponseDTO buildCartResponse() {
        CartItemDTO item = new CartItemDTO();
        item.setBookId(1L);
        item.setBookName("Book");
        item.setImageUrl("img");
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantity(1);

        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartItems(List.of(item));
        dto.setCartTotal(BigDecimal.valueOf(100));
        return dto;
    }

    private Order buildOrder(Long id) {
        Order order = new Order();
        order.setId(id);
        order.setUserId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(BigDecimal.valueOf(100));

        OrderItem item = new OrderItem();
        item.setBookId(1L);
        item.setBookName("Book");
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantity(1);
        item.setOrder(order);

        order.setOrderItems(List.of(item));
        return order;
    }

    private FeignException feignNotFound() {
        return new FeignException.NotFound(
                "not found", mock(Request.class), null, null);
    }

    private FeignException feignUnauthorized() {
        return new FeignException.Unauthorized(
                "unauthorized", mock(Request.class), null, null);
    }

    private FeignException feignServiceUnavailable() {
        return new FeignException.ServiceUnavailable(
                "down", mock(Request.class), null, null);
    }
}
