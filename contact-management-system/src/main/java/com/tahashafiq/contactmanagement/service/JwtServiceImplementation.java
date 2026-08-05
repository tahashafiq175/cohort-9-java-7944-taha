package com.tahashafiq.contactmanagement.service;

import com.tahashafiq.contactmanagement.entity.UserEntity;
import com.tahashafiq.contactmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImplementation implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity byUserName = userRepository.findByUserName(username);
        System.out.println("FOUND USER: " + byUserName.getUserName());
        System.out.println("PASSWORD FROM DB: " + byUserName.getPassword());
        if (byUserName != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(byUserName.getUserName())
                    .password(byUserName.getPassword())  // already hashed
                    .roles(byUserName.getRoles())
                    .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
