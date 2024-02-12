package com.notehub.notehub.service.role;

import java.util.Optional;

import com.notehub.notehub.entity.Role;

public interface RoleService {

    Optional<Role> findByName(String authority);

    Role createRole(Role userRole);
}
