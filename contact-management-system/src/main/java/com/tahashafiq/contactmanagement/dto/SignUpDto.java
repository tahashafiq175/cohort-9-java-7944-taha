package com.tahashafiq.contactmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    String firstName;
    String lastName;
    String email;
    String userName;
    String password;
    String roles;
}
