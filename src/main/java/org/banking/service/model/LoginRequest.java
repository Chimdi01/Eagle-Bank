package org.banking.service.model;

/**
 * Request model for user login and JWT generation.
 */
public class LoginRequest {
    private String userId;

    /**
     * Gets the user ID for login.
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for login.
     * @param userId the user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
} 