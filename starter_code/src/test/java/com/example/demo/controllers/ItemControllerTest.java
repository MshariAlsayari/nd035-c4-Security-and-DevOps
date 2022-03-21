package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private static Item item;


    @BeforeClass
    public static void init() {
        initItem();
    }


    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_item_by_id_test(){
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        final ResponseEntity<Item> response = itemController.getItemById(1L);
        Item itemBody = response.getBody();

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        Assert.assertNotNull(itemBody);
        assertEquals(item.getId(),itemBody.getId());

    }

    @Test
    public void get_item_by_name_test(){
        when(itemRepository.findByName(item.getName())).thenReturn(Collections.singletonList(item));

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Pen");
        List<Item> items = response.getBody();

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        Assert.assertNotNull(items);
        assertEquals(item.getId(),items.get(0).getId());

    }

    private static void initItem(){
        item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setDescription("blue color");
        item.setPrice(new BigDecimal("1.00"));
    }

}
