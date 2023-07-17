package com.api.warung.controller;

import com.api.warung.dto.AuthToken;
import com.api.warung.dto.LoginDto;
import com.api.warung.dto.RegisterDto;
import com.api.warung.dto.ResponData;
import com.api.warung.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/warung/api/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<ResponData<AuthToken>> userRegister(@Valid @RequestBody RegisterDto registerDto,
                                                              Errors errors){
        ResponData<AuthToken> responData = new ResponData<>();
        try {
            if (errors.hasErrors()){
                for (ObjectError error : errors.getAllErrors()){
                    responData.getMessage().add(error.getDefaultMessage());
                }
                responData.setStatus(false);
                responData.setPayload(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responData);
            }else {
                responData.setStatus(true);
                responData.getMessage().add("Register success");
                responData.setPayload(userService.saveUser(registerDto));
                return ResponseEntity.ok(responData);
            }
        }catch (RuntimeException exception){
            responData.setStatus(false);
            responData.setPayload(null);
            responData.getMessage().add(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responData);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ResponData<AuthToken>> loginUser(@Valid @RequestBody LoginDto loginDto,
                                                           Errors errors){
        ResponData<AuthToken> responData = new ResponData<>();
        try{
            if (errors.hasErrors()){
                for (ObjectError error : errors.getAllErrors()){
                    responData.getMessage().add(error.getDefaultMessage());
                }
                responData.setStatus(false);
                responData.setPayload(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responData);
            }else {
                responData.setStatus(true);
                responData.setPayload(userService.loginValidate(loginDto));
                return ResponseEntity.ok(responData);
            }
        }catch (NoSuchElementException exception){
            responData.setStatus(false);
            responData.setPayload(null);
            responData.getMessage().add(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responData);
        }
    }
}
