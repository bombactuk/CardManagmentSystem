package com.bank.cardManagementSystem.service;

import com.bank.cardManagementSystem.dto.RegistrationUserDto;
import com.bank.cardManagementSystem.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User createNewUser(RegistrationUserDto registrationUserDto);

}
