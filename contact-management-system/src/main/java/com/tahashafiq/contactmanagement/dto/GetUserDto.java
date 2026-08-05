package com.tahashafiq.contactmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class GetUserDto {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> phoneNumbers=new ArrayList<>();
}
