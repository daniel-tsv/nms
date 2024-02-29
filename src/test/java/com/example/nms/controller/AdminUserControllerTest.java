package com.example.nms.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.AdminUserDTO;
import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminUserController adminUserController;

    private MockMvc mockMvc;

    private AdminUserDTO user1;
    private AdminUserDTO user2;
    private AdminUserDTO user3;

    private List<AdminUserDTO> users;

    private ObjectMapper mapper;

    @BeforeEach
    void setUpBeforeEach() {

        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        user1 = new AdminUserDTO(UUID.randomUUID(), "John", "john@email", 3,
                Set.of(new Role(UUID.randomUUID(), "ROLE_USER")), Instant.now().minus(Duration.ofDays(1)), true, false,
                true, true);
        user2 = new AdminUserDTO(UUID.randomUUID(), "Bob", "bob@email", 1,
                new LinkedHashSet<>(
                        Set.of(new Role(UUID.randomUUID(), "ROLE_USER"), new Role(UUID.randomUUID(), "ROLE_ADMIN"))),
                Instant.now().minus(Duration.ofDays(365)), true,
                true, true, true);
        user3 = new AdminUserDTO(UUID.randomUUID(), "Miku", "miku@email", 7,
                Set.of(new Role(UUID.randomUUID(), "ROLE_USER")), Instant.now().minus(Duration.ofDays(30)), true, true,
                true, false);

        users = List.of(user1, user2, user3);
    }

    @Test
    void findAllShouldReturnAllUsersAsAdminUserDTOs() throws Exception {

        Page<User> page = new PageImpl<>(Collections.singletonList(new User()));

        when(userService.listUsers(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);
        when(userMapper.toAdminUserDTO(anyList())).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/admin/users") // assuming this is the mapping
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .param("sort", "uuid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<AdminUserDTO> actualUsers = mapper.readValue(content, new TypeReference<List<AdminUserDTO>>() {
        });

        assertEquals(users, actualUsers);
    }

    @Test
    void findUserByUUIDShouldReturnUserAsAdminUserDTO() throws Exception {

        when(userService.findById(user1.getUuid())).thenReturn(Optional.of(new User()));
        when(userMapper.toAdminUserDTO(any(User.class))).thenReturn(user1);

        MvcResult result = mockMvc.perform(get("/admin/users/{id}", user1.getUuid()))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AdminUserDTO actualUser = mapper.readValue(content, AdminUserDTO.class);

        assertEquals(user1, actualUser);
    }

    @Test
    void findUserByUUIDShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

        UUID nonExistentUserId = UUID.randomUUID();

        when(userService.findById(nonExistentUserId))
                .thenThrow(new UserIdNotFoundException(
                        String.format(MessageConstants.USER_ID_NOT_FOUND, nonExistentUserId)));

        try {
            mockMvc.perform(get("/admin/users/{id}", nonExistentUserId));
            fail(String.format(MessageConstants.EXPECTED_EXCEPTION_WAS_NOT_THROWN, "UserIdNotFoundException"));
        } catch (ServletException e) {
            assertInstanceOf(UserIdNotFoundException.class, e.getRootCause());
        }

    }

    @Test
    void updateUserShouldReturnUserAsAdminUserDTO() throws Exception {

        when(userService.updateUserFromAdminDTO(eq(user3.getUuid()), any(AdminUserDTO.class))).thenReturn(new User());
        when(userMapper.toAdminUserDTO(any(User.class))).thenReturn(user3);

        MvcResult result = mockMvc.perform(patch("/admin/users/{id}", user3.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user3)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AdminUserDTO actualUser = mapper.readValue(content, AdminUserDTO.class);

        assertEquals(user3, actualUser);

    }

    @Test
    void updateUserShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

        UUID nonExistentUserId = UUID.randomUUID();

        when(userService.updateUserFromAdminDTO(eq(nonExistentUserId), any(AdminUserDTO.class)))
                .thenThrow(new UserIdNotFoundException(
                        String.format(MessageConstants.USER_ID_NOT_FOUND, nonExistentUserId)));

        try {
            mockMvc.perform(patch("/admin/users/{id}", nonExistentUserId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(user3)));
            fail(String.format(MessageConstants.EXPECTED_EXCEPTION_WAS_NOT_THROWN, "UserIdNotFoundException"));
        } catch (ServletException e) {
            assertInstanceOf(UserIdNotFoundException.class, e.getRootCause());
        }

    }

    @Test
    void deleteUserShouldReturnNoContentIfUserWasSuccessfullyDeleted() throws Exception {

        when(userService.delete(user1.getUuid())).thenReturn(true);

        mockMvc.perform(delete("/admin/users/{id}", user1.getUuid()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

        UUID nonExistentUserId = UUID.randomUUID();

        when(userService.delete(nonExistentUserId)).thenReturn(false);

        try {
            mockMvc.perform(delete("/admin/users/{id}", nonExistentUserId));
            fail(String.format(MessageConstants.EXPECTED_EXCEPTION_WAS_NOT_THROWN, "UserIdNotFoundException"));
        } catch (ServletException e) {
            assertInstanceOf(UserIdNotFoundException.class, e.getRootCause());
        }
    }

    @Test
    void assignRoleShouldReturnUserAsAdminUserDTO() throws Exception {

        UUID roleUuid = UUID.randomUUID();

        when(userService.assignRole(user2.getUuid(), roleUuid)).thenReturn(new User());
        when(userMapper.toAdminUserDTO(any(User.class))).thenReturn(user2);

        MvcResult result = mockMvc
                .perform(post("/admin/users/{userId}/roles/{roleId}", user2.getUuid(), roleUuid))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AdminUserDTO actualUser = mapper.readValue(content, AdminUserDTO.class);

        assertEquals(user2, actualUser);
    }

    @Test
    void assignRoleShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {
        UUID nonExistentUserId = UUID.randomUUID();
        UUID roleUuid = UUID.randomUUID();

        when(userService.assignRole(nonExistentUserId, roleUuid))
                .thenThrow(new UserIdNotFoundException(
                        String.format(MessageConstants.USER_ID_NOT_FOUND, nonExistentUserId)));
        try {
            mockMvc.perform(post("/admin/users/{userId}/roles/{roleId}", nonExistentUserId, roleUuid));
            fail(String.format(MessageConstants.EXPECTED_EXCEPTION_WAS_NOT_THROWN, "UserIdNotFoundException"));
        } catch (ServletException e) {
            assertInstanceOf(UserIdNotFoundException.class, e.getRootCause());
        }
    }

    @Test
    void assignRoleShouldThrowRoleIdNotFoundWhenRoleDoesNotExist() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID nonExistentRoleId = UUID.randomUUID();

        when(userService.assignRole(userId, nonExistentRoleId))
                .thenThrow(
                        new RoleIdNotFoundException(
                                String.format(MessageConstants.ROLE_ID_NOT_FOUND, nonExistentRoleId)));
        try {
            mockMvc.perform(post("/admin/users/{userId}/roles/{roleId}", userId, nonExistentRoleId));
            fail(String.format(MessageConstants.EXPECTED_EXCEPTION_WAS_NOT_THROWN, "RoleIdNotFoundException"));
        } catch (ServletException e) {
            assertInstanceOf(RoleIdNotFoundException.class, e.getRootCause());
        }
    }

    @Test
    void removeRoleShouldReturnUserAsAdminUserDTO() throws Exception {

        UUID roleUuid = UUID.randomUUID();

        when(userService.removeRole(user1.getUuid(), roleUuid)).thenReturn(new User());
        when(userMapper.toAdminUserDTO(any(User.class))).thenReturn(user1);

        MvcResult result = mockMvc
                .perform(delete("/admin/users/{userId}/roles/{roleId}", user1.getUuid(), roleUuid))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AdminUserDTO actualUser = mapper.readValue(content, AdminUserDTO.class);

        assertEquals(user1, actualUser);
    }

    @Test
    void removeRoleShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {
        UUID nonExistentUserId = UUID.randomUUID();
        UUID roleUuid = UUID.randomUUID();

        when(userService.removeRole(nonExistentUserId, roleUuid))
                .thenThrow(new UserIdNotFoundException(
                        String.format(MessageConstants.USER_ID_NOT_FOUND, nonExistentUserId)));
        try {
            mockMvc.perform(delete("/admin/users/{userId}/roles/{roleId}", nonExistentUserId, roleUuid));
            fail(String.format(MessageConstants.EXPECTED_EXCEPTION_WAS_NOT_THROWN, "UserIdNotFoundException"));
        } catch (ServletException e) {
            assertInstanceOf(UserIdNotFoundException.class, e.getRootCause());
        }
    }

    @Test
    void removeRoleShouldThrowRoleIdNotFoundWhenRoleDoesNotExist() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID nonExistentRoleId = UUID.randomUUID();

        when(userService.removeRole(userId, nonExistentRoleId))
                .thenThrow(
                        new RoleIdNotFoundException(
                                String.format(MessageConstants.ROLE_ID_NOT_FOUND, nonExistentRoleId)));
        try {
            mockMvc.perform(delete("/admin/users/{userId}/roles/{roleId}", userId, nonExistentRoleId));
            fail(String.format(MessageConstants.EXPECTED_EXCEPTION_WAS_NOT_THROWN, "RoleIdNotFoundException"));
        } catch (ServletException e) {
            assertInstanceOf(RoleIdNotFoundException.class, e.getRootCause());
        }
    }
}
