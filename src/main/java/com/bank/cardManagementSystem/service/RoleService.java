package com.bank.cardManagementSystem.service;

import com.bank.cardManagementSystem.entity.Role;
import com.bank.cardManagementSystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

}
