package com.tahashafiq.contactmanagement.controller;
import com.tahashafiq.contactmanagement.dto.LoginDto;
import com.tahashafiq.contactmanagement.dto.SignUpDto;
import com.tahashafiq.contactmanagement.entity.UserEntity;
import com.tahashafiq.contactmanagement.impl.UserServiceImpl;
import com.tahashafiq.contactmanagement.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserServiceImpl userService;
    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signup(@RequestBody SignUpDto postUserDto) {
        UserEntity userEntity= userService.createUser(postUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        System.out.println(
                passwordEncoder.matches(
                        "Usamashafiq123",
                        "$2a$10$Eeb34QDF5EhH7VHp1S7WYen97fORGvR7yQ9qyo/.7TZo00dCE6Sde"
                )
        );
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUserName(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserEntity byUserName = userService.findByUserName(loginDto.getUserName());
        if(byUserName==null){
            throw  new UsernameNotFoundException(loginDto.getUserName());
        }
        String Token = jwtUtils.generateToken(
                byUserName.getUserName(),
                byUserName.getRoles()
        );
        return ResponseEntity.ok(Token);
    }
}
