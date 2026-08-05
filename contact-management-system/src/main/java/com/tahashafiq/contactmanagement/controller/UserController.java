package com.tahashafiq.contactmanagement.controller;
import com.tahashafiq.contactmanagement.dto.GetUserDto;
import com.tahashafiq.contactmanagement.dto.SignUpDto;
import com.tahashafiq.contactmanagement.entity.UserEntity;
import com.tahashafiq.contactmanagement.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<GetUserDto> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userService.findById(userId));
    }
}
