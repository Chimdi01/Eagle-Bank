package org.banking.service.controller;

import org.banking.service.model.*;
import org.banking.service.service.UserService;
import org.banking.service.util.ErrorUtil;
import org.banking.service.util.ValidationUtil;
import org.banking.service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user management endpoints.
 * Provides endpoints to create, fetch, and update users.
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

    /**
     * Constructs a UserController with the given services.
     * @param userService the user service
     * @param accountService the account service
     */
    @Autowired
    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    /**
     * Creates a new user.
     * @param request the user creation request
     * @return the created user response
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            ValidationUtil.validateCreateUserRequest(request);
            UserResponse user = userService.createUser(request);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            String field = null;
            if (msg.contains("userId")) field = "userId";
            else if (msg.contains("phoneNumber")) field = "phoneNumber";
            else if (msg.contains("email")) field = "email";
            BadRequestErrorResponse error = ErrorUtil.badRequest(
                msg,
                java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Fetches a user by userId.
     * @param userId the user ID
     * @param authHeader the Authorization header
     * @return the user response
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> fetchUser(@PathVariable String userId, @RequestHeader("Authorization") String authHeader) {
        try {
            ValidationUtil.validateUserId(userId);
            ValidationUtil.validateAuthHeader(authHeader, userId);
            UserResponse user = userService.fetchUser(userId);
            if (user == null) {
                return new ResponseEntity<>(ErrorUtil.error("User not found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            String field = null;
            if (msg.contains("userId")) field = "userId";
            else if (msg.contains("phoneNumber")) field = "phoneNumber";
            else if (msg.contains("email")) field = "email";
            BadRequestErrorResponse error = ErrorUtil.badRequest(
                msg,
                java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing user.
     * @param userId the user ID
     * @param authHeader the Authorization header
     * @param request the update request
     * @return the updated user response
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestHeader("Authorization") String authHeader, @RequestBody UpdateUserRequest request) {
        try {
            ValidationUtil.validateUserId(userId);
            ValidationUtil.validateAuthHeader(authHeader, userId);
            ValidationUtil.validateUpdateUserRequest(request);
            UserResponse user = userService.updateUser(userId, request);
            if (user == null) {
                return new ResponseEntity<>(ErrorUtil.error("User not found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            String field = null;
            if (msg.contains("userId")) field = "userId";
            else if (msg.contains("phoneNumber")) field = "phoneNumber";
            else if (msg.contains("email")) field = "email";
            BadRequestErrorResponse error = ErrorUtil.badRequest(
                msg,
                java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
