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
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.CategoryResponse;
import com.food.order.restful.model.CategoryWithFoodResponse;
import com.food.order.restful.model.RegisterCategoryRequest;
import com.food.order.restful.model.UpdateCategoryRequest;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.repository.CategoryRepository;
import com.food.order.restful.repository.FoodRepository;
import com.food.order.restful.repository.OrderRepository;
import com.food.order.restful.repository.ProfileRepository;
import com.food.order.restful.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnableWebMvc
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderRepository orderRepository;

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
    }

    @Test
    void testCreateCategorySuccess() throws Exception {
        RegisterCategoryRequest request = new RegisterCategoryRequest();
        request.setName("Main Course");

        mockMvc.perform(
                post("/api/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testCreateCategoryDuplicate() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        RegisterCategoryRequest request = new RegisterCategoryRequest();
        request.setName("Main Course");

        mockMvc.perform(
                post("/api/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateCategoryBlank() throws Exception {        
        RegisterCategoryRequest request = new RegisterCategoryRequest();
        request.setName("");

        mockMvc.perform(
                post("/api/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateCategoryTokenNotSent() throws Exception {        
        RegisterCategoryRequest request = new RegisterCategoryRequest();
        request.setName("");

        mockMvc.perform(
                post("/api/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))                                            
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCategorySuccess() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        mockMvc.perform(
                get("/api/category/" + category.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testGetCategoryBadId() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        mockMvc.perform(
                get("/api/category/123test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCategoryNotFound() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        mockMvc.perform(
                get("/api/category/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCategorySuccess() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Main Course");

        mockMvc.perform(
                put("/api/category/" + category.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
            assertEquals(request.getName(), response.getData().getName());
        });
    }

    @Test
    void testUpdateCategoryDuplicate() throws Exception {
        CategoryEntity mainCourse = new CategoryEntity();
        mainCourse.setName("Main Course");
        categoryRepository.save(mainCourse);

        CategoryEntity sideDish = new CategoryEntity();
        sideDish.setName("Side Dish");
        categoryRepository.save(sideDish);

        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Side Dish");

        mockMvc.perform(
                put("/api/category/" + mainCourse.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                      
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCategoryTokenNotSent() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Main Course");

        mockMvc.perform(
                put("/api/category/" + category.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))                                              
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCategoryBadId() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Main Course");

        mockMvc.perform(
                put("/api/category/123test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                                              
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCategoryNotFound() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Main Course");

        mockMvc.perform(
                put("/api/category/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")                                              
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCategoryWithFoodSuccess() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                get("/api/category/" + category.getId() + "/foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<CategoryWithFoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }
}
