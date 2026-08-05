package com.tahashafiq.contactmanagement.impl;

import com.tahashafiq.contactmanagement.dto.GetUserDto;
import com.tahashafiq.contactmanagement.dto.SignUpDto;
import com.tahashafiq.contactmanagement.entity.ContactEntity;
import com.tahashafiq.contactmanagement.entity.UserEntity;
import com.tahashafiq.contactmanagement.repository.UserRepository;
import com.tahashafiq.contactmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;
    @Override
    public List<GetUserDto> findAllUsers() {
        List<UserEntity> allUser = userRepository.findAll();
        List<GetUserDto> getUserDto=new ArrayList<>();
        for(UserEntity user:allUser){
            getUserDto.add(mapToDto(user));
        }
        return getUserDto;
    }


    @Override
    public GetUserDto findById(String userId) {
        return mapToDto(findEntityById(userId));
    }

    public UserEntity findEntityById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public UserEntity createUser(SignUpDto postUserDto) {
        UserEntity userEntity = mapToEntity(postUserDto);
        return userRepository.save(userEntity);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }


    @Override
    public void deleteUser(String userId) {

        userRepository.deleteById(userId);
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        return null;
    }

    @Override
    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }




    private GetUserDto mapToDto(UserEntity user) {
        GetUserDto dto = new GetUserDto();

        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setPassword(user.getPassword());
        for(ContactEntity contactEntity:user.getContactEntities()){
            dto.getPhoneNumbers().add(contactEntity.getPhoneNumber());
        }
        return dto;
    }
    private UserEntity mapToEntity(SignUpDto dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setUserName(dto.getUserName());
        userEntity.setEmail(dto.getEmail());
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        if(dto.getRoles()!=null){
            userEntity.setRoles(dto.getRoles());
        }else{
            userEntity.setRoles("USER");
        }
        return  userEntity;
    }

    public UserEntity findEntityByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
