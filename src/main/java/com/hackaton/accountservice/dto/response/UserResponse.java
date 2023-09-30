package com.hackaton.accountservice.dto.response;

import com.hackaton.accountservice.model.AccountModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String passportId;
    private String country;
    private LocalDate dateOfBirth;
    private int age;
    private AccountModel account;
}
