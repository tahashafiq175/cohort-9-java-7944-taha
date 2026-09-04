package com.tahashafiq.contactmanagement.controller;
import com.tahashafiq.contactmanagement.exceptionhandling.ResourceNotFoundException;
import com.tahashafiq.contactmanagement.dto.LoginDto;
import com.tahashafiq.contactmanagement.dto.SignUpDto;
import com.tahashafiq.contactmanagement.entity.UserEntity;
import com.tahashafiq.contactmanagement.impl.UserServiceImpl;
import com.tahashafiq.contactmanagement.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/public")
@Tag(name="Public Apis")
public class PublicController {

    final PasswordEncoder passwordEncoder;

    final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final UserServiceImpl userService;
    PublicController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserServiceImpl userService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }
    @PostMapping("/signup")
    @Operation(summary = "Sign Up The User")
    @ApiResponse(responseCode = "201", description = "Journal created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid journal data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UserEntity> signup(@Valid @RequestBody SignUpDto postUserDto) {
        UserEntity userEntity= userService.createUser(postUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }
    @PostMapping("/login")
    @Operation(summary = "Sign In The User")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {

        new LoginDto();
        LoginDto updatedLoginDto;
        updatedLoginDto=userService.signInOption(loginDto);
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        updatedLoginDto.getUserName(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserEntity byUserName = userService.findEntityByUserName(updatedLoginDto.getUserName());
        if(byUserName==null){
            throw  new ResourceNotFoundException("Username corresponding to that request not found");
        }
        String token = jwtUtils.generateToken(
                byUserName.getUserName(),
                byUserName.getRoles()
        );
        return ResponseEntity.ok(token);
    }
}
