package com.bank.cardManagementSystem.service.impl;

import com.bank.cardManagementSystem.entity.Role;
import com.bank.cardManagementSystem.repository.RoleRepository;
import com.bank.cardManagementSystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

}
