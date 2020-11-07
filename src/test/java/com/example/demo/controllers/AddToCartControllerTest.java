package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserApplication;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddToCartControllerTest {

    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitOrderUser()
    {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));

        UserApplication user = new UserApplication();
        user.setUsername("Dimi");
        user.setPassword("test");
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("t-shirt");
        item.setDescription("this is a new t-shirt");
        item.setPrice(BigDecimal.valueOf(10.0));

        List<Item> listItems = new ArrayList<>();
        listItems.add(item);

        cart.setItems(listItems);
        user.setCart(cart);
        cart.setUser(user);

        when(userRepository.findByUsername("Dimi")).thenReturn(user);

        ResponseEntity<UserOrder> responseEntity = orderController.submit(user.getUsername());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        UserOrder userOrder = responseEntity.getBody();

        Assert.assertNotNull(userOrder);
        Assert.assertNotNull(userOrder.getItems());
        Assert.assertNotNull(userOrder.getUser());
        Assert.assertNotNull(userOrder.getTotal());
    }
}
