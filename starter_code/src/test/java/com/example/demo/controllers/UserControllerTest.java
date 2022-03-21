package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class UserControllerTest {


    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder= mock(BCryptPasswordEncoder.class);



    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }


    @Test
    public void create_user_test(){
        when(bCryptPasswordEncoder.encode("123")).thenReturn("thisIsHashed");
        CreateUserRequest request =getUserRequest();

        ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();

        Assert.assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(user);
        assertEquals(request.getUsername(), user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }


    @Test
    public void find_user_by_username(){
        when(bCryptPasswordEncoder.encode("123")).thenReturn("thisIsHashed");
        CreateUserRequest request =getUserRequest();

        ResponseEntity<User> createdUserResponse = userController.createUser(request);
        User createdUser = createdUserResponse.getBody();

        Assert.assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());
        Assert.assertNotNull(createdUser);
        assertEquals(request.getUsername(), createdUser.getUsername());
        assertEquals("thisIsHashed", createdUser.getPassword());

        when(userRepository.findByUsername(createdUser.getUsername())).thenReturn(createdUser);
        final ResponseEntity<User> response2 = userController.findByUserName(createdUser.getUsername());
        User userFound = response2.getBody();
        assertEquals(200, response2.getStatusCodeValue());

    }

    @Test
    public void find_user_by_id(){
        when(bCryptPasswordEncoder.encode("123")).thenReturn("thisIsHashed");
        CreateUserRequest request =getUserRequest();

        ResponseEntity<User> createdUserResponse = userController.createUser(request);
        User createdUser = createdUserResponse.getBody();

        Assert.assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());
        Assert.assertNotNull(createdUser);
        assertEquals(request.getUsername(), createdUser.getUsername());
        assertEquals("thisIsHashed", createdUser.getPassword());

        when(userRepository.findById(createdUser.getId())).thenReturn(Optional.of(createdUser));
        final ResponseEntity<User> response2 = userController.findById(createdUser.getId());
        User userFound = response2.getBody();
        assertEquals(200, response2.getStatusCodeValue());

    }



    private CreateUserRequest getUserRequest(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("kjahdsflkshdflf");
        request.setPassword("123");
        return request;

    }
}
