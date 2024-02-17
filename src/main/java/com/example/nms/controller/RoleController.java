package com.example.nms.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nms.constants.MessageConstants;
import com.example.nms.entity.Role;
import com.example.nms.exception.role.InvalidRoleException;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.service.role.RoleService;
import com.example.nms.validator.RoleValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleValidator roleValidator;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody @Valid Role role, BindingResult br) {

        roleValidator.validate(role, br);
        if (br.hasErrors())
            throw new InvalidRoleException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        return ResponseEntity.ok(roleService.create(role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(
                roleService.findById(id).orElseThrow(
                        () -> new RoleIdNotFoundException(
                                String.format(MessageConstants.ROLE_ID_NOT_FOUND, id))));
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") UUID id, @RequestBody @Valid Role role,
            BindingResult br) {

        role.setUuid(id);
        roleValidator.validate(role, br);
        if (br.hasErrors())
            throw new InvalidRoleException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        return ResponseEntity.of(roleService.updateById(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") UUID id) {

        if (roleService.delete(id))
            return ResponseEntity.noContent().build();

        throw new RoleIdNotFoundException(
                String.format(MessageConstants.ROLE_NOT_FOUND, id));
    }
}
