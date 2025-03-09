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
import com.food.order.restful.entity.ProfileEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.ProfileResponse;
import com.food.order.restful.model.RegisterProfileRequest;
import com.food.order.restful.model.UpdateProfileRequest;
import com.food.order.restful.model.WebResponse;
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
public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("test");        
        user.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 1));
        userRepository.save(user);
    }

    @Test
    void testCreateProfileSuccess() throws Exception {
        RegisterProfileRequest request = new RegisterProfileRequest();
        request.setFirstname("Bintang");
        request.setLastname("Ginanjar");
        request.setEmail("email@mail.com");
        request.setAddress("Pasirluyu");
        request.setPhoneNumber("123456");
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");
        request.setPostalCode("40254");      

        mockMvc.perform(
                post("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testCreateProfileBlank() throws Exception {
        RegisterProfileRequest request = new RegisterProfileRequest();
        request.setFirstname("");
        request.setLastname("");
        request.setEmail("");
        request.setAddress("Pasirluyu");
        request.setPhoneNumber("123456");
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");
        request.setPostalCode("40254");      

        mockMvc.perform(
                post("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateProfileInvalidToken() throws Exception {
        RegisterProfileRequest request = new RegisterProfileRequest();
        request.setFirstname("Bintang");
        request.setLastname("Ginanjar");
        request.setEmail("email@mail.com");
        request.setAddress("Pasirluyu");
        request.setPhoneNumber("123456");
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");
        request.setPostalCode("40254");       

        mockMvc.perform(
                post("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "notfound")                       
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateProfileTokenNotSent() throws Exception {
        RegisterProfileRequest request = new RegisterProfileRequest();
        request.setFirstname("");
        request.setLastname("");
        request.setEmail("");
        request.setAddress("Pasirluyu");
        request.setPhoneNumber("123456");
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");
        request.setPostalCode("40254");      

        mockMvc.perform(
                post("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))                                          
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetProfileSuccess() throws Exception {
        UserEntity user = userRepository.findByEmail("test").orElse(null);

        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname("Bintang");
        profile.setLastname("Ginanjar");
        profile.setEmail("email@mail.com");
        profile.setAddress("Pasirluyu");
        profile.setPhoneNumber("123456");
        profile.setCity("Bandung");
        profile.setProvince("Jawa Barat");
        profile.setPostalCode("40254");
        profile.setUserEntity(user);  
        profileRepository.save(profile);

        mockMvc.perform(
                get("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testGetProfileInvalidToken() throws Exception {
        UserEntity user = userRepository.findByEmail("test").orElse(null);

        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname("Bintang");
        profile.setLastname("Ginanjar");
        profile.setEmail("email@mail.com");
        profile.setAddress("Pasirluyu");
        profile.setPhoneNumber("123456");
        profile.setCity("Bandung");
        profile.setProvince("Jawa Barat");
        profile.setPostalCode("40254");
        profile.setUserEntity(user);  
        profileRepository.save(profile);

        mockMvc.perform(
                get("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("X-API-TOKEN", "invalid")                       
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetProfileTokenNotSent() throws Exception {
        UserEntity user = userRepository.findByEmail("test").orElse(null);

        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname("Bintang");
        profile.setLastname("Ginanjar");
        profile.setEmail("email@mail.com");
        profile.setAddress("Pasirluyu");
        profile.setPhoneNumber("123456");
        profile.setCity("Bandung");
        profile.setProvince("Jawa Barat");
        profile.setPostalCode("40254");
        profile.setUserEntity(user);  
        profileRepository.save(profile);

        mockMvc.perform(
                get("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateProfileSuccess() throws Exception {
        UserEntity user = userRepository.findByEmail("test").orElse(null);

        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname("Bintang");;
        profile.setLastname("Ginanjar");
        profile.setEmail("email@mail.com");
        profile.setAddress("Pasirluyu");
        profile.setPhoneNumber("123456");
        profile.setCity("Bandung");
        profile.setProvince("Jawa Barat");
        profile.setPostalCode("40254");
        profile.setUserEntity(user);  
        profileRepository.save(profile);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstname("Dimas");
        request.setLastname("Al Rasyid Ginanjar");
        request.setEmail("email@mail.com");
        request.setAddress("Pasirluyu");
        request.setPhoneNumber("56789");
        request.setCity("Bandung");
        request.setProvince("Jawa Barat");
        request.setPostalCode("40254");      

        mockMvc.perform(
                patch("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
            assertNull(response.getErrors());
            assertEquals(request.getFirstname(), response.getData().getFirstname());
            assertEquals(request.getLastname(), response.getData().getLastname());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertEquals(request.getAddress(), response.getData().getAddress());
            assertEquals(request.getPhoneNumber(), response.getData().getPhoneNumber());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());
        });
    }

    @Test
    void testUpdateProfileWrongEmail() throws Exception {
        UserEntity user = userRepository.findByEmail("test").orElse(null);

        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname("Bintang");;
        profile.setLastname("Ginanjar");
        profile.setEmail("email@mail.com");
        profile.setAddress("Pasirluyu");
        profile.setPhoneNumber("123456");
        profile.setCity("Bandung");
        profile.setProvince("Jawa Barat");
        profile.setPostalCode("40254");
        profile.setUserEntity(user);  
        profileRepository.save(profile);

        UpdateProfileRequest request = new UpdateProfileRequest();        
        request.setEmail("emailcom");             

        mockMvc.perform(
                patch("/api/profiles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) 
                        .header("X-API-TOKEN", "test")                       
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<ProfileResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
            assertNotNull(response.getErrors());
        });
    }
}
