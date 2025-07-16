package org.banking.service.user;

import org.banking.service.controller.UserController;
import org.banking.service.model.*;
import org.banking.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.banking.service.util.JwtUtil;
import org.banking.service.service.AccountService;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

    private String getAuthHeader() {
        return "Bearer " + JwtUtil.generateToken("usr-abc123");
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Test User");
        UserResponse resp = new UserResponse();
        resp.setId("usr-abc123");
        resp.setName("Test User");
        when(userService.createUser(any())).thenReturn(resp);

        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test User\",\"address\":{\"line1\":\"A\",\"town\":\"B\",\"county\":\"C\",\"postcode\":\"D\"},\"phoneNumber\":\"+1234567890\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("usr-abc123"));
    }

    @Test
    void testFetchUser() throws Exception {
        UserResponse resp = new UserResponse();
        resp.setId("usr-abc123");
        resp.setName("Test User");
        when(userService.fetchUser("usr-abc123")).thenReturn(resp);
        mockMvc.perform(get("/v1/users/usr-abc123")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("usr-abc123"));
    }

    @Test
    void testFetchUser_invalidUserId() throws Exception {
        mockMvc.perform(get("/v1/users/invalid")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid userId format"))
                .andExpect(jsonPath("$.details[0].field").value("userId"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserRequest req = new UpdateUserRequest();
        req.setName("Updated");
        UserResponse resp = new UserResponse();
        resp.setId("usr-abc123");
        resp.setName("Updated");
        when(userService.updateUser(eq("usr-abc123"), any())).thenReturn(resp);
        mockMvc.perform(patch("/v1/users/usr-abc123")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testUpdateUser_invalidUserId() throws Exception {
        mockMvc.perform(patch("/v1/users/invalid")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid userId format"))
                .andExpect(jsonPath("$.details[0].field").value("userId"));
    }

    @Test
    void testUpdateUser_invalidPhoneNumber() throws Exception {
        mockMvc.perform(patch("/v1/users/usr-abc123")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phoneNumber\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("phoneNumber"));
    }

    @Test
    void testUpdateUser_invalidEmail() throws Exception {
        mockMvc.perform(patch("/v1/users/usr-abc123")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("email"));
    }

    @Test
    void testCreateUser_invalidPhoneNumber() throws Exception {
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test User\",\"address\":{\"line1\":\"A\",\"town\":\"B\",\"county\":\"C\",\"postcode\":\"D\"},\"phoneNumber\":\"123\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("phoneNumber"));
    }

    @Test
    void testCreateUser_invalidEmail() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Test User");
        UserResponse resp = new UserResponse();
        resp.setId("usr-abc123");
        resp.setName("Test User");
        when(userService.createUser(any())).thenReturn(resp);
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test User\",\"address\":{\"line1\":\"A\",\"town\":\"B\",\"county\":\"C\",\"postcode\":\"D\"},\"phoneNumber\":\"+1234567890\",\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("email"));
    }
} 