package org.banking.service.model;

/**
 * Model for a simple error response with a message.
 */
public class ErrorResponse {
    private String message;

    /**
     * Default constructor.
     */
    public ErrorResponse() {}

    /**
     * Gets the error message.
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     * @param message the error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
} 