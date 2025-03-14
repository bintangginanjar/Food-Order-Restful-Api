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
import com.food.order.restful.model.FoodResponse;
import com.food.order.restful.model.RegisterFoodRequest;
import com.food.order.restful.model.UpdateFoodRequest;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.repository.CategoryRepository;
import com.food.order.restful.repository.FoodRepository;
import com.food.order.restful.repository.ProfileRepository;
import com.food.order.restful.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnableWebMvc
@SpringBootTest
@AutoConfigureMockMvc
public class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("test");        
        user.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 1));
        userRepository.save(user);

        CategoryEntity category = new CategoryEntity();
        category.setName("Main Course");
        categoryRepository.save(category);
    }

    @Test
    void testCreateFoodSuccess() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        RegisterFoodRequest request = new RegisterFoodRequest();
        request.setName("Fried rice");
        request.setPrice(40);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                post("/api/categories/" + category.getId() + "/foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testCreateFoodBlankRequest() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        RegisterFoodRequest request = new RegisterFoodRequest();
        request.setName("");
        request.setPrice(null);
        request.setPhotoUrl("");

        mockMvc.perform(
                post("/api/categories/" + category.getId() + "/foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateFoodTokenNotSent() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        RegisterFoodRequest request = new RegisterFoodRequest();
        request.setName("Fried rice");
        request.setPrice(40);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                post("/api/categories/" + category.getId() + "/foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "invalidtoken")                       
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateFoodCategoryNotFound() throws Exception {    
        RegisterFoodRequest request = new RegisterFoodRequest();
        request.setName("Fried rice");
        request.setPrice(40);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                post("/api/categories/123/foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateFoodBadCategory() throws Exception {    
        RegisterFoodRequest request = new RegisterFoodRequest();
        request.setName("Fried rice");
        request.setPrice(40);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                post("/api/categories/123test/foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetFoodSuccess() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                get("/api/categories/" + category.getId() + "/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testGetFoodBadCategoryId() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                get("/api/categories/123test/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetFoodBadCategoryNotFound() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                get("/api/categories/123/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetFoodBadFoodId() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                get("/api/categories/" + category.getId() + "/foods/123test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetFoodBadFoodNotFound() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
            get("/api/categories/" + category.getId() + "/foods/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateFoodSuccess() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        UpdateFoodRequest request = new UpdateFoodRequest();
        request.setName("Beef Fried rice");
        request.setPrice(45);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                patch("/api/categories/" + category.getId() + "/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testUpdateFoodBadCategory() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        UpdateFoodRequest request = new UpdateFoodRequest();
        request.setName("Beef Fried rice");
        request.setPrice(45);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                patch("/api/categories/123test/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateFoodCategoryNotFound() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        UpdateFoodRequest request = new UpdateFoodRequest();
        request.setName("Beef Fried rice");
        request.setPrice(45);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                patch("/api/categories/123/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateFoodBadFood() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        UpdateFoodRequest request = new UpdateFoodRequest();
        request.setName("Beef Fried rice");
        request.setPrice(45);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                patch("/api/categories/" + category.getId() + "/foods/123test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateFoodNotFound() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        UpdateFoodRequest request = new UpdateFoodRequest();
        request.setName("Beef Fried rice");
        request.setPrice(45);
        request.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");

        mockMvc.perform(
                patch("/api/categories/" + category.getId() + "/foods/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteFoodSuccess() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                delete("/api/categories/" + category.getId() + "/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
            assertFalse(foodRepository.existsById(food.getId()));
        });
    }

    @Test
    void testDeleteFoodBadCategory() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                delete("/api/categories/123test/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteFoodCategoryNotFound() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                delete("/api/categories/123/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteFoodBadFood() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                delete("/api/categories/" + category.getId() + "/foods/123test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteFoodNotFound() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                delete("/api/categories" + category.getId() + "foods/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteFoodTokenNotSent() throws Exception {
        CategoryEntity category = categoryRepository.findByName("Main Course").orElse(null);

        FoodEntity food = new FoodEntity();
        food.setName("Fried rice");
        food.setPrice(40);
        food.setPhotoUrl("https://img.freepik.com/free-photo/american-shrimp-fried-rice-served-with-chili-fish-sauce-thai-food_1150-26576.jpg");
        food.setCategoryEntity(category);
        foodRepository.save(food);

        mockMvc.perform(
                delete("/api/categories/" + category.getId() + "/foods/" + food.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                                                                    
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<FoodResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());            
        });
    }
}
