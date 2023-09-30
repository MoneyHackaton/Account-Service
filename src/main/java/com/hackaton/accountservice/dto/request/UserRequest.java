package com.hackaton.accountservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String email;
    private LocalDate registryDate;
    private String avatarImageUri;
    private Long income;
    private String firstName;
    private String lastName;
    private String passportId;
    private String country;
    private LocalDate dateOfBirth;
    private LocalDate dateExpiration;
}
