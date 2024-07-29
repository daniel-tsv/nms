package com.example.nms.service.role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.nms.entity.Role;

public interface RoleService {

    Role create(Role userRole);

    Optional<Role> findById(UUID id);

    Optional<Role> findByName(String authority);

    List<Role> findAll();

    Role updateById(UUID id, Role role);

    void delete(UUID id);
}
