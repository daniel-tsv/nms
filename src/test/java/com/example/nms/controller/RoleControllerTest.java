package com.example.nms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import com.example.nms.constants.RoleConstants;
import com.example.nms.entity.Role;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.exception.role.RoleValidationException;
import com.example.nms.service.role.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {

    @MockBean
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Faker faker;

    private Role exampleRole;
    private List<Role> exampleRoles;

    @BeforeEach
    void setUp() {
        exampleRole = createExampleRole();
        exampleRoles = IntStream.range(0, 5).mapToObj(i -> createExampleRole()).toList();
    }

    private Role createExampleRole() {
        return new Role(UUID.randomUUID(), "ROLE_" + faker.lorem().word());
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void createRoleShouldReturnCreatedRole() throws Exception {

        when(roleService.create(exampleRole)).thenReturn(exampleRole);

        String content = mapper.writeValueAsString(exampleRole);

        mockMvc.perform(post("/admin/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));

        verify(roleService).create(exampleRole);
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void createRoleShouldThrowRoleValidationWhenRoleHasErrors() throws Exception {

        Role badRole = new Role("bad role name");

        mockMvc.perform(post("/admin/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badRole)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleValidationException));
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void createRoleShouldThrowRoleValidationWhenRoleWithSameNameAlreadyExists() throws Exception {

        Role existingRole = exampleRole;
        Role newRole = new Role(existingRole.getName());

        when(roleService.findByName(newRole.getName())).thenReturn(Optional.of(existingRole));

        mockMvc.perform(post("/admin/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newRole)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleValidationException));
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void findRoleShouldReturnExistingRole() throws Exception {

        UUID existingRoleId = exampleRole.getUuid();

        when(roleService.findById(existingRoleId)).thenReturn(Optional.of(exampleRole));

        mockMvc.perform(get("/admin/roles/{id}", existingRoleId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exampleRole)));
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void findRoleShouldThrowRoleIdNotFoundWhenRoleWithSelectedIdDoesNotExists() throws Exception {

        UUID nonExistentRoleId = UUID.randomUUID();

        when(roleService.findById(nonExistentRoleId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/roles/{id}", nonExistentRoleId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleIdNotFoundException));
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void getAllRolesShouldReturnAllRoles() throws Exception {

        when(roleService.findAll()).thenReturn(exampleRoles);

        mockMvc.perform(get("/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exampleRoles)));

        verify(roleService).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void updateRoleShouldReturnUpdatedRole() throws Exception {

        UUID idOfRoleToUpdate = exampleRole.getUuid();

        when(roleService.updateById(idOfRoleToUpdate, exampleRole)).thenReturn(exampleRole);

        String content = mapper.writeValueAsString(exampleRole);

        mockMvc.perform(patch("/admin/roles/{id}", idOfRoleToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));

        verify(roleService).updateById(idOfRoleToUpdate, exampleRole);
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void updateRoleShouldThrowRoleIdNotFoundWhenRoleWithSelectedIdDoesNotExists() throws Exception {

        UUID nonExistentRoleId = exampleRole.getUuid();

        doThrow(new RoleIdNotFoundException(nonExistentRoleId))
                .when(roleService).updateById(nonExistentRoleId, exampleRole);

        mockMvc.perform(patch("/admin/roles/{id}", nonExistentRoleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(exampleRole)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleIdNotFoundException));

        verify(roleService).updateById(nonExistentRoleId, exampleRole);
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void updateRoleShouldThrowRoleValidationIfRoleWithSameNameAlreadyExists() throws Exception {

        UUID idOfRoleToUpdate = exampleRole.getUuid();
        String nameOfAlreadyExistingRole = exampleRole.getName();

        doThrow(new RoleValidationException(mock(BindingResult.class))).when(roleService)
                .findByName(nameOfAlreadyExistingRole);

        mockMvc.perform(patch("/admin/roles/{id}", idOfRoleToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(exampleRole)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleValidationException));

        verify(roleService).findByName(nameOfAlreadyExistingRole);
        verify(roleService, never()).updateById(idOfRoleToUpdate, exampleRole);
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void updateRoleShouldThrowRoleValidationIfRoleHasErrors() throws Exception {

        UUID idOfRoleToUpdate = exampleRole.getUuid();
        Role badRole = new Role("bad role name");

        mockMvc.perform(patch("/admin/roles/{id}", idOfRoleToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badRole)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleValidationException));

        verify(roleService, never()).updateById(idOfRoleToUpdate, exampleRole);
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void deleteRoleShouldReturnNoContentOnSuccessfulDeletion() throws Exception {

        UUID idOfRoleToDelete = UUID.randomUUID();

        mockMvc.perform(delete("/admin/roles/{id}", idOfRoleToDelete))
                .andExpect(status().isNoContent());

        verify(roleService).delete(idOfRoleToDelete);
    }

    @Test
    @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
    void deleteRoleShouldThrowRoleIdNotFoundWhenRoleWithSelectedIdDoesNotExists() throws Exception {

        UUID nonExistentRoleId = UUID.randomUUID();

        doThrow(new RoleIdNotFoundException(nonExistentRoleId)).when(roleService).delete(nonExistentRoleId);

        mockMvc.perform(delete("/admin/roles/{id}", nonExistentRoleId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleIdNotFoundException));

        verify(roleService).delete(nonExistentRoleId);
    }
}