package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserApplication;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController ;

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void SetUp(){
        cartController = new CartController();

        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));

        UserApplication user = new UserApplication();
        user.setId(1L);
        user.setUsername("Dimi");
        user.setPassword("test");
        user.setCart(cart);


        Item item = new Item();
        item.setId(1L);
        item.setName("t-shirt");
        item.setDescription("this is a new t-shirt");
        item.setPrice(BigDecimal.valueOf(10.0));

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> responseCart = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseCart);
        assertEquals(200,responseCart.getStatusCodeValue());

        Cart cart2 = responseCart.getBody();

        assertNotNull(responseCart);
        List<Item> items = cart2.getItems();
        assertNotNull(items);
        assertEquals("Dimi", cart2.getUser().getUsername());

        verify(cartRepository, times(1)).save(cart2);
    }

    @Test
    public void addToCartNotNullUser(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));

        UserApplication user = new UserApplication();
        user.setId(1L);
        user.setUsername(null);
        user.setPassword("test");
        user.setCart(cart);


        Item item = new Item();
        item.setId(1L);
        item.setName("t-shirt");
        item.setDescription("this is a new t-shirt");
        item.setPrice(BigDecimal.valueOf(10.0));

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("Dimi");

        ResponseEntity<Cart> responseCart = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseCart);
        assertEquals(404,responseCart.getStatusCodeValue());
    }

    @Test
    public void addToCartWrongItem()
    {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));

        UserApplication user = new UserApplication();
        user.setId(1L);
        user.setUsername("Dimi");
        user.setPassword("test");
        user.setCart(cart);


        Item item = new Item();
        item.setId(1L);
        item.setName("t-shirt");
        item.setDescription("this is a new t-shirt");
        item.setPrice(BigDecimal.valueOf(10.0));

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(3L);
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> responseCart = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseCart);
        assertEquals(404,responseCart.getStatusCodeValue());
    }

    @Test
    public void removeToCart(){

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

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("Dimi");


        ResponseEntity<Cart> responseCart = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseCart);
        assertEquals(200,responseCart.getStatusCodeValue());

        Cart cart2 = responseCart.getBody();

        assertNotNull(responseCart);
        List<Item> items = cart2.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
        assertEquals("Dimi", cart2.getUser().getUsername());

        verify(cartRepository, times(1)).save(cart2);
    }

    @Test
    public void removeWrongItem()
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

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(3L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("Dimi");


        ResponseEntity<Cart> responseCart = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseCart);
        assertEquals(404,responseCart.getStatusCodeValue());
    }

    @Test
    public void removeItemNullUser()
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

        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(3L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("Dimi");


        ResponseEntity<Cart> responseCart = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseCart);
        assertEquals(404,responseCart.getStatusCodeValue());
    }
}
