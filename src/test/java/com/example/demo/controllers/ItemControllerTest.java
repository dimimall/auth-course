package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItemsTest(){
        List<Item> itemList;

        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200,responseEntity.getStatusCodeValue());
        itemList = responseEntity.getBody();
        Assert.assertNotNull(itemList);
    }

    @Test
    public void getItemById(){
        Item item = new Item();
        item.setId(1L);
        item.setName("t-shirt");
        item.setDescription("this is a new t-shirt");
        item.setPrice(BigDecimal.valueOf(10.0));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> responseEntity = itemController.getItemById(item.getId());
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200,responseEntity.getStatusCodeValue());

        Assert.assertNotNull(item);
    }

    @Test
    public void getItemByName(){
        List<Item> itemList = new ArrayList<>();

        Item item = new Item();
        item.setId(1L);
        item.setName("t-shirt");
        item.setDescription("this is a new t-shirt");
        item.setPrice(BigDecimal.valueOf(10.0));

        itemList.add(item);

        when(itemRepository.findByName(item.getName())).thenReturn(itemList);

        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(item.getName());
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200,responseEntity.getStatusCodeValue());
        itemList = responseEntity.getBody();
        Assert.assertEquals(itemList,responseEntity.getBody());
    }
}
