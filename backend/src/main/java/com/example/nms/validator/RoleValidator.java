package com.example.nms.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.nms.constants.MessageConstants;
import com.example.nms.entity.Role;
import com.example.nms.service.role.RoleService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleValidator implements Validator {

    private final RoleService roleService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Role.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Role role = (Role) target;

        roleService.findByName(role.getName()).ifPresent(existingRole -> {
            if (role.getUuid() == null || !role.getUuid().equals(existingRole.getUuid()))
                errors.rejectValue("name", "role.name.exists",
                        String.format(MessageConstants.ROLE_NAME_EXISTS, role.getName()));
        });
    }

}
