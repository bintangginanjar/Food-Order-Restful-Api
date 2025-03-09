package com.food.order.restful.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.LoginUserRequest;
import com.food.order.restful.model.TokenResponse;
import com.food.order.restful.repository.UserRepository;


@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ValidationService validationService;

    public AuthService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        UserEntity user = userRepository.findByEmail(request.getUsername())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 1));
            userRepository.save(user);

            return TokenResponse.builder().token(user.getToken()).expiredAt(user.getTokenExpiredAt()).build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        }
    }

    @Transactional
    public void logout(UserEntity user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }
}
