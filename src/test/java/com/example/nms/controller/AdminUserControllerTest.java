package com.example.nms.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nms.constants.RoleConstants;
import com.example.nms.dto.AdminUserDTO;
import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
class AdminUserControllerTest {

        @MockBean
        private UserService userService;

        @MockBean
        private UserMapper userMapper;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper mapper;

        @Autowired
        private Faker faker;

        private User exampleUser;
        private AdminUserDTO exampleUserDTO;
        private List<AdminUserDTO> exampleUserDTOs;

        private UUID nonExistentUserId;
        private UUID nonExistentRoleId;

        @BeforeEach
        void setUp() {
                exampleUser = new User();
                exampleUserDTO = createExampleUserDTO();

                exampleUserDTOs = new ArrayList<>();
                for (int i = 0; i < 11; i++)
                        exampleUserDTOs.add(createExampleUserDTO());

                nonExistentRoleId = UUID.randomUUID();
                nonExistentUserId = UUID.randomUUID();
        }

        private AdminUserDTO createExampleUserDTO() {

                return new AdminUserDTO(
                                UUID.randomUUID(),
                                faker.internet().username(),
                                faker.internet().emailAddress(),
                                faker.random().nextInt(30),
                                Collections.singleton(new Role(UUID.randomUUID(), RoleConstants.ROLE_USER)),
                                faker.date().past(faker.random().nextInt(1, 365), TimeUnit.DAYS).toInstant(),
                                true,
                                true,
                                true,
                                true);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void findAllShouldReturnAllUsersAsAdminUserDTOs() throws Exception {

                List<User> users = Collections.singletonList(exampleUser);
                Page<User> page = new PageImpl<>(users);

                String sortParam = "uuid";
                String directionParam = "asc";
                int pageParam = 0;
                int sizeParam = 10;

                when(userService.listUsers(pageParam, sizeParam, directionParam, sortParam)).thenReturn(page);
                when(userMapper.toAdminUserDTO(users)).thenReturn(exampleUserDTOs);

                mockMvc.perform(get("/admin/users")
                                .param("page", String.valueOf(pageParam))
                                .param("size", String.valueOf(sizeParam))
                                .param("direction", directionParam)
                                .param("sort", sortParam))
                                .andExpect(status().isOk())
                                .andExpect(content().json(mapper.writeValueAsString(exampleUserDTOs)))
                                .andExpect(jsonPath("$", hasSize(exampleUserDTOs.size())));

                verify(userService).listUsers(pageParam, sizeParam, directionParam, sortParam);
                verify(userMapper).toAdminUserDTO(users);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void findUserByUUIDShouldReturnUserAsAdminUserDTO() throws Exception {

                UUID uuid = exampleUserDTO.getUuid();

                when(userService.findById(uuid)).thenReturn(Optional.of(exampleUser));
                when(userMapper.toAdminUserDTO(exampleUser)).thenReturn(exampleUserDTO);

                mockMvc.perform(get("/admin/users/{id}", uuid))
                                .andExpect(status().isOk())
                                .andExpect(content().json(mapper.writeValueAsString(exampleUserDTO)));

                verify(userService).findById(uuid);
                verify(userMapper).toAdminUserDTO(exampleUser);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void findUserByUUIDShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

                doThrow(new UserIdNotFoundException(nonExistentUserId))
                                .when(userService).findById(nonExistentUserId);

                mockMvc.perform(get("/admin/users/{id}", nonExistentUserId))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIdNotFoundException));

                verify(userService).findById(nonExistentUserId);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void updateUserShouldReturnUserAsAdminUserDTO() throws Exception {

                UUID uuid = exampleUserDTO.getUuid();

                when(userService.updateFromAdminDTO(uuid, exampleUserDTO)).thenReturn(exampleUser);
                when(userMapper.toAdminUserDTO(exampleUser)).thenReturn(exampleUserDTO);

                String expectedContent = mapper.writeValueAsString(exampleUserDTO);

                mockMvc.perform(patch("/admin/users/{id}", uuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(expectedContent))
                                .andExpect(status().isOk())
                                .andExpect(content().json(expectedContent));

                verify(userService).updateFromAdminDTO(uuid, exampleUserDTO);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void updateUserShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

                doThrow(new UserIdNotFoundException(nonExistentUserId))
                                .when(userService).updateFromAdminDTO(nonExistentUserId, exampleUserDTO);

                mockMvc.perform(patch("/admin/users/{id}", nonExistentUserId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(exampleUserDTO)))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIdNotFoundException));

                verify(userService).updateFromAdminDTO(nonExistentUserId, exampleUserDTO);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void deleteUserShouldReturnNoContentIfUserWasSuccessfullyDeleted() throws Exception {

                UUID uuid = UUID.randomUUID();

                mockMvc.perform(delete("/admin/users/{id}", uuid))
                                .andExpect(status().isNoContent());

                verify(userService).deleteById(uuid);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void deleteUserShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

                doThrow(new UserIdNotFoundException(nonExistentUserId))
                                .when(userService).deleteById(nonExistentUserId);

                mockMvc.perform(delete("/admin/users/{id}", nonExistentUserId))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIdNotFoundException));

                verify(userService).deleteById(nonExistentUserId);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void assignRoleShouldReturnUserAsAdminUserDTO() throws Exception {

                UUID userId = exampleUserDTO.getUuid();
                UUID roleId = UUID.randomUUID();

                when(userService.assignRole(userId, roleId)).thenReturn(exampleUser);
                when(userMapper.toAdminUserDTO(exampleUser)).thenReturn(exampleUserDTO);

                mockMvc.perform(post("/admin/users/{userId}/roles/{roleId}", userId, roleId))
                                .andExpect(status().isOk())
                                .andExpect(content().json(mapper.writeValueAsString(exampleUserDTO)));

                verify(userService).assignRole(userId, roleId);
                verify(userMapper).toAdminUserDTO(exampleUser);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void assignRoleShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

                UUID roleId = UUID.randomUUID();

                doThrow(new UserIdNotFoundException(nonExistentUserId))
                                .when(userService).assignRole(nonExistentUserId, roleId);

                mockMvc.perform(post("/admin/users/{userId}/roles/{roleId}", nonExistentUserId, roleId))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIdNotFoundException));

                verify(userService).assignRole(nonExistentUserId, roleId);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void assignRoleShouldThrowRoleIdNotFoundWhenRoleDoesNotExist() throws Exception {

                UUID userId = UUID.randomUUID();

                doThrow(new RoleIdNotFoundException(nonExistentRoleId))
                                .when(userService).assignRole(userId, nonExistentRoleId);

                mockMvc.perform(post("/admin/users/{userId}/roles/{roleId}", userId, nonExistentRoleId))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleIdNotFoundException));

                verify(userService).assignRole(userId, nonExistentRoleId);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void removeRoleShouldReturnUserAsAdminUserDTO() throws Exception {

                UUID userId = exampleUserDTO.getUuid();
                UUID roleId = UUID.randomUUID();

                when(userService.removeRole(userId, roleId)).thenReturn(exampleUser);
                when(userMapper.toAdminUserDTO(exampleUser)).thenReturn(exampleUserDTO);

                mockMvc.perform(delete("/admin/users/{userId}/roles/{roleId}", userId, roleId))
                                .andExpect(status().isOk())
                                .andExpect(content().json(mapper.writeValueAsString(exampleUserDTO)));

                verify(userService).removeRole(userId, roleId);
                verify(userMapper).toAdminUserDTO(exampleUser);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void removeRoleShouldThrowUserIdNotFoundWhenUserDoesNotExist() throws Exception {

                UUID roleId = UUID.randomUUID();

                doThrow(new UserIdNotFoundException(nonExistentUserId))
                                .when(userService).removeRole(nonExistentUserId, roleId);

                mockMvc.perform(delete("/admin/users/{userId}/roles/{roleId}", nonExistentUserId, roleId))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIdNotFoundException));

                verify(userService).removeRole(nonExistentUserId, roleId);
        }

        @Test
        @WithMockUser(username = "admin", roles = { RoleConstants.ADMIN })
        void removeRoleShouldThrowRoleIdNotFoundWhenRoleDoesNotExist() throws Exception {

                UUID userId = UUID.randomUUID();

                doThrow(new RoleIdNotFoundException(nonExistentRoleId))
                                .when(userService).removeRole(userId, nonExistentRoleId);

                mockMvc.perform(delete("/admin/users/{userId}/roles/{roleId}", userId, nonExistentRoleId))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleIdNotFoundException));

                verify(userService).removeRole(userId, nonExistentRoleId);
        }
}
