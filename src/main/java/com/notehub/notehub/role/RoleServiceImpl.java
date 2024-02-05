package com.notehub.notehub.role;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByAuthority(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role createRole(Role userRole) {
        return roleRepository.save(userRole);
    }
}
