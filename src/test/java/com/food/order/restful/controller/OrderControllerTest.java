package com.food.order.restful.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.entity.FoodEntity;
import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.entity.OrderItemEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.UpdateOrderItemRequest;
import com.food.order.restful.model.OrderResponse;
import com.food.order.restful.model.OrderWithItemResponse;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.repository.CategoryRepository;
import com.food.order.restful.repository.FoodRepository;
import com.food.order.restful.repository.OrderItemRepository;
import com.food.order.restful.repository.OrderRepository;
import com.food.order.restful.repository.ProfileRepository;
import com.food.order.restful.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EnableWebMvc
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setName("Test");
        user.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 1));
        userRepository.save(user);

        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setCode(UUID.randomUUID().toString());
        food.setIsReady(true);
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        FoodEntity burger = new FoodEntity();
        burger.setName("Burger");
        burger.setCode(UUID.randomUUID().toString());
        burger.setIsReady(true);
        burger.setPrice(10);
        burger.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        burger.setCategoryEntity(category);
        foodRepository.save(burger);
    }

    @Test
    void testCreateOrderSuccess() throws Exception {                
        mockMvc.perform(
                post("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<OrderResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testListOrderSuccess() throws Exception {  
        UserEntity user = userRepository.findByUsername("test").orElse(null);

        OrderEntity order = new OrderEntity();

        Date date = new Date();

        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(date.toString());
        order.setTotalPrice(0);
        order.setStatus("Pending");
        order.setUserEntity(user);
        orderRepository.save(order);

        mockMvc.perform(
                get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<List<OrderResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testUpdateOrderItemAddSuccess() throws Exception {  
        Date date = new Date(); 
        UserEntity user = userRepository.findByUsername("test").orElse(null);
        //CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);
        FoodEntity food = foodRepository.findByName("Fried rice").orElse(null);

        OrderEntity order = new OrderEntity();        
        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(date.toString());
        order.setTotalPrice(0);
        order.setStatus("Pending");
        order.setUserEntity(user);
        orderRepository.save(order);
        
        UpdateOrderItemRequest request = new UpdateOrderItemRequest();
        request.setQuantity(2);        
        
        mockMvc.perform(
                patch("/api/orders/"+ order.getId() + "/food/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)   
                        .content(objectMapper.writeValueAsString(request))                     
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<OrderResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testUpdateOrderItemSubstractSuccess() throws Exception {  
        Date date = new Date(); 
        UserEntity user = userRepository.findByUsername("test").orElse(null);
        //CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);
        FoodEntity food = foodRepository.findByName("Fried rice").orElse(null);

        OrderEntity order = new OrderEntity();        
        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(date.toString());
        order.setTotalPrice(0);
        order.setStatus("Pending");
        order.setUserEntity(user);
        orderRepository.save(order);
        
        UpdateOrderItemRequest request = new UpdateOrderItemRequest();
        request.setQuantity(-1);        
        
        mockMvc.perform(
                patch("/api/orders/"+ order.getId() + "/food/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)   
                        .content(objectMapper.writeValueAsString(request))                     
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<OrderResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testDeleteOrderItemSuccess() throws Exception {
        Date date = new Date(); 
        UserEntity user = userRepository.findByUsername("test").orElse(null);
        //CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);
        FoodEntity food = foodRepository.findByName("Fried rice").orElse(null);

        OrderEntity order = new OrderEntity();        
        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(date.toString());
        order.setTotalPrice(0);
        order.setStatus("Pending");
        order.setUserEntity(user);
        orderRepository.save(order);
        
        OrderItemEntity item = new OrderItemEntity();
        item.setQuantity(2);
        item.setSubTotal(80);
        item.setFoodEntity(food);
        item.setOrderEntity(order);
        orderItemRepository.save(item);
        
        mockMvc.perform(
                delete("/api/orders/" + order.getId() + "/item/" + item.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                                               
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<OrderResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
            assertFalse(orderItemRepository.existsById(item.getId()));
        });
    }

    @Test
    void testGetOrderItemSuccess() throws Exception {
        Date date = new Date(); 
        UserEntity user = userRepository.findByUsername("test").orElse(null);
        //CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);
        FoodEntity food = foodRepository.findByName("Fried rice").orElse(null);
        FoodEntity burger = foodRepository.findByName("Burger").orElse(null);

        OrderEntity order = new OrderEntity();
        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(date.toString());
        order.setTotalPrice(90);
        order.setStatus("Pending");
        order.setUserEntity(user);
        orderRepository.save(order);
        
        OrderItemEntity item = new OrderItemEntity();
        item.setQuantity(2);
        item.setSubTotal(80);
        item.setFoodEntity(food);
        item.setOrderEntity(order);
        orderItemRepository.save(item);

        OrderItemEntity side = new OrderItemEntity();
        side.setQuantity(3);
        side.setSubTotal(10);
        side.setFoodEntity(burger);
        side.setOrderEntity(order);
        orderItemRepository.save(side);
        
        mockMvc.perform(
                get("/api/orders/" + order.getId() + "/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                                               
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<OrderWithItemResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());            
        });
    }
}
