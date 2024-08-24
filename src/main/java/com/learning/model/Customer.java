package com.learning.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    private UUID uuid;

    private String username;

    private String fullName;

    private String accountNumber;

    private String address;

    private String aadharNumber;

    private GenderType gender;

    private AccountType accountType;

    private double balance;

    private String phoneNumber;

    private String password;
}
