package com.tahashafiq.contactmanagement.impl;

import com.tahashafiq.contactmanagement.exceptionhandling.ResourceNotFoundException;
import com.tahashafiq.contactmanagement.dto.GetUserDto;
import com.tahashafiq.contactmanagement.dto.LoginDto;
import com.tahashafiq.contactmanagement.dto.SignUpDto;
import com.tahashafiq.contactmanagement.entity.ContactEntity;
import com.tahashafiq.contactmanagement.entity.UserEntity;
import com.tahashafiq.contactmanagement.provider.AuthProvider;
import com.tahashafiq.contactmanagement.repository.UserRepository;
import com.tahashafiq.contactmanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

   private final UserRepository userRepository;

   UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
       this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
   }
    @Override
    public LoginDto signInOption(LoginDto loginDto) {
        if ((loginDto.getUserName() == null ||
                        loginDto.getUserName().isBlank())
                        &&
                        loginDto.getEmail() != null &&
                        !loginDto.getEmail().isBlank()) {

            UserEntity user =
                    userRepository.findByEmail(
                            loginDto.getEmail()
                    );

            if (user == null) {
                throw new ResourceNotFoundException(
                        "User not found with this email"
                );
            }

            return maptoLoginDto(user);
        }

        return loginDto;
    }


    @Override
    public List<GetUserDto> findAllUsers() {
        List<UserEntity> allUser = userRepository.findAll();
        List<GetUserDto> getUserDto=new ArrayList<>();
        for(UserEntity user:allUser){
            getUserDto.add(mapToDto(user));
        }
        log.info("returning all Users {}", getUserDto);
        return getUserDto;
    }


    @Override
    public GetUserDto findById(String userId) {
        log.info("finding a user corresponding to {}",userId);
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
        log.info("deleting a user corresponding to {}",userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserEntity updateUser(UserEntity byUserName,SignUpDto updatedUser) {
        log.info("request recieved to update user {} ",byUserName);
        log.info("updating user {}",byUserName);
        if(byUserName != null){
            if(updatedUser.getUserName()!=null && !updatedUser.getUserName().isEmpty()){
                byUserName.setUserName(updatedUser.getUserName());
            }
            if(updatedUser.getFirstName()!=null && !updatedUser.getFirstName().isEmpty()){
                byUserName.setFirstName(updatedUser.getFirstName());
            }
            if(updatedUser.getLastName()!=null && !updatedUser.getLastName().isEmpty()){
                byUserName.setLastName(updatedUser.getLastName());
            }
            if(updatedUser.getEmail()!=null && !updatedUser.getEmail().isEmpty()){
                byUserName.setEmail(updatedUser.getEmail());
            }
            if(updatedUser.getPassword()!=null && !updatedUser.getPassword().isEmpty()){
                byUserName.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
        }
        return userRepository.save(byUserName);
    }

    @Override
    public GetUserDto findByUserName(String userName) {
        return mapToDto(findEntityByUserName(userName));
    }

    public UserEntity findEntityByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    //  Mapping Logic as if we does not want to show the user who request all the detials/
    // so we simply create separate dtos containing the fields that we want to return
    // to the user  //


    private GetUserDto mapToDto(UserEntity user) {
        log.info("mapping UserEntity against getUserDto {}", user);

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
        log.info("returning UserDto after mapping {}", dto);
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
        userEntity.setAuthProvider(AuthProvider.LOCAL);
        if(dto.getUserName().equals("TahaShafiq175")){
            userEntity.setRoles("ADMIN");
        }else{
            userEntity.setRoles("USER");
        }
        return  userEntity;
    }

    @Override
    public UserEntity manageGoogleUser(String firstName, String lastName, String email, String providerId) {

        Optional<UserEntity> byAuthProviderAndProviderId = userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, providerId);
        if(byAuthProviderAndProviderId.isPresent()){
            return  byAuthProviderAndProviderId.get();
        }

        UserEntity userEntity=UserEntity.builder()
                .userId(providerId)
                .userName(email)
                .email(email)
                .roles("USER")
                .firstName(firstName)
                .lastName(lastName)
                .providerId(providerId)
                .authProvider(AuthProvider.GOOGLE)
                .build();

     return userRepository.save(userEntity);
    }

    private LoginDto maptoLoginDto(UserEntity byEmail) {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(byEmail.getEmail());
        loginDto.setPassword(byEmail.getPassword());
        loginDto.setUserName(byEmail.getUserName());
        return  loginDto;
    }


}
