/*
 * Implementation of the UserService interface for managing user data in memory.
 * Provides methods to create, fetch, and update users.
 */
package org.banking.service.user;

import org.banking.service.model.*;
import org.banking.service.service.UserService;
import org.banking.service.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

/**
 * Service implementation for user management.
 * Stores users in a thread-safe in-memory map.
 */
@Service
public class UserServiceImpl implements UserService {
    private final Map<String, UserResponse> users = new ConcurrentHashMap<>();
    private final AccountService accountService;

    /**
     * Constructs a UserServiceImpl with the given AccountService dependency.
     * @param accountService the account service used for user-account operations
     */
    public UserServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new user and stores it in memory.
     * @param request the user creation request
     * @return the created user response
     */
    @Override
    public UserResponse createUser(CreateUserRequest request) {
        String userId = generateUserId();
        OffsetDateTime now = OffsetDateTime.now();
        UserResponse user = new UserResponse();
        user.setId(userId);
        user.setName(request.getName());
        user.setAddress(mapAddress(request.getAddress()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setCreatedTimestamp(now);
        user.setUpdatedTimestamp(now);
        users.put(userId, user);
        return user;
    }

    /**
     * Fetches a user by userId.
     * @param userId the user ID
     * @return the user response, or null if not found
     */
    @Override
    public UserResponse fetchUser(String userId) {
        return users.get(userId);
    }

    /**
     * Updates an existing user with new data.
     * @param userId the user ID
     * @param request the update request
     * @return the updated user response, or null if not found
     */
    @Override
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        UserResponse user = users.get(userId);
        if (user != null) {
            if (request.getName() != null) user.setName(request.getName());
            if (request.getAddress() != null) user.setAddress(mapAddress(request.getAddress()));
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            user.setUpdatedTimestamp(OffsetDateTime.now());
        }
        return user;
    }

    /**
     * Generates a unique user ID.
     * @return a new user ID string
     */
    private String generateUserId() {
        return "usr-" + UUID.randomUUID().toString().replace("-", "").substring(0, 7);
    }

    /**
     * Maps a CreateUserRequest.Address to a UserResponse.Address.
     * @param address the address from the request
     * @return the mapped address, or null if input is null
     */
    private UserResponse.Address mapAddress(CreateUserRequest.Address address) {
        if (address == null) return null;
        UserResponse.Address addr = new UserResponse.Address();
        addr.setLine1(address.getLine1());
        addr.setLine2(address.getLine2());
        addr.setLine3(address.getLine3());
        addr.setTown(address.getTown());
        addr.setCounty(address.getCounty());
        addr.setPostcode(address.getPostcode());
        return addr;
    }

    /**
     * Maps an UpdateUserRequest.Address to a UserResponse.Address.
     * @param address the address from the update request
     * @return the mapped address, or null if input is null
     */
    private UserResponse.Address mapAddress(UpdateUserRequest.Address address) {
        if (address == null) return null;
        UserResponse.Address addr = new UserResponse.Address();
        addr.setLine1(address.getLine1());
        addr.setLine2(address.getLine2());
        addr.setLine3(address.getLine3());
        addr.setTown(address.getTown());
        addr.setCounty(address.getCounty());
        addr.setPostcode(address.getPostcode());
        return addr;
    }
} 