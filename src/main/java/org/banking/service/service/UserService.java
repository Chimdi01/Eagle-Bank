/*
 * UserService interface for user management operations.
 * Defines methods for creating, fetching, and updating users.
 */
package org.banking.service.service;

import org.banking.service.model.*;

/**
 * Service interface for user management.
 */
public interface UserService {
    /**
     * Creates a new user.
     * @param request the user creation request
     * @return the created user response
     */
    UserResponse createUser(CreateUserRequest request);

    /**
     * Fetches a user by userId.
     * @param userId the user ID
     * @return the user response, or null if not found
     */
    UserResponse fetchUser(String userId);

    /**
     * Updates an existing user.
     * @param userId the user ID
     * @param request the update request
     * @return the updated user response, or null if not found
     */
    UserResponse updateUser(String userId, UpdateUserRequest request);
} 