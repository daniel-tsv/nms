package com.example.nms.service.role;

import java.util.Optional;

import com.example.nms.entity.Role;

public interface RoleService {

    Optional<Role> findByName(String authority);

    Role createRole(Role userRole);
}
