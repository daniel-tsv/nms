package com.example.nms.service.role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.entity.Role;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {

        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role create(Role role) {

        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(UUID id) {

        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {

        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role updateById(UUID id, Role role) {

        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new RoleIdNotFoundException(id));
        existingRole.setName(role.getName());

        return roleRepository.save(existingRole);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        if (!roleRepository.existsById(id))
            throw new RoleIdNotFoundException(id);

        roleRepository.deleteById(id);
    }
}
