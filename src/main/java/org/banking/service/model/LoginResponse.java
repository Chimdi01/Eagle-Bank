package org.banking.service.model;

/**
 * Response model containing a JWT token after successful authentication.
 */
public class LoginResponse {
    private String token;

    /**
     * Constructs a LoginResponse with the given token.
     * @param token the JWT token
     */
    public LoginResponse(String token) {
        this.token = token;
    }

    /**
     * Gets the JWT token.
     * @return the JWT token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the JWT token.
     * @param token the JWT token
     */
    public void setToken(String token) {
        this.token = token;
    }
} 