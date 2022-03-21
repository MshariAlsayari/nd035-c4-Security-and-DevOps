package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    private UserController userController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final UserRepository userRepository = mock(UserRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder= mock(BCryptPasswordEncoder.class);


    private static User user;
    private static Item item;
    private static Cart cart;

    @BeforeClass
    public static void init() {
        initUser();
        initItem();
    }







    @Before
    public void setUp(){
        cartController = new CartController();
        userController = new UserController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        initCart();

    }

    @Test
    public void add_to_cart_test(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        ModifyCartRequest request = getModifyCartRequest();


        final ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cartBody = response.getBody();

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        Assert.assertNotNull(cartBody);
        assertEquals(user,cartBody.getUser());
        assertEquals(item,cartBody.getItems().get(0));

    }

    @Test
    public void remove_from_cart_test(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        ModifyCartRequest request = getModifyCartRequest();


        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Cart cartBody = response.getBody();

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        Assert.assertNotNull(cartBody);
        assertEquals(0,cartBody.getItems().size());
    }




    private static void initUser(){
        user = new User();
        user.setId(1L);
        user.setUsername("mshari");
    }

    private static void initItem(){
        item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setDescription("blue color");
        item.setPrice(new BigDecimal("1.00"));
    }

    private void initCart(){
        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);
    }


    private ModifyCartRequest getModifyCartRequest(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("mshari");
        request.setItemId(1);
        request.setQuantity(2);
        return request;
    }



}
