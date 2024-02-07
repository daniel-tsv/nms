package com.notehub.notehub.services.role;

import java.util.Optional;

import com.notehub.notehub.entities.Role;

public interface RoleService {

    Optional<Role> findByAuthority(String authority);

    Role createRole(Role userRole);
}
