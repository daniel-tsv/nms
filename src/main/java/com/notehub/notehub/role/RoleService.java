package com.notehub.notehub.role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findByAuthority(String authority);

    Role createRole(Role userRole);
}
