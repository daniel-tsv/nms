package com.notehub.notehub.services.role;

import java.util.Optional;

import com.notehub.notehub.entities.Role;

public interface RoleService {

    Optional<Role> findByName(String authority);

    Role createRole(Role userRole);
}
