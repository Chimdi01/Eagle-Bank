package org.banking.service.model;

import java.util.List;

/**
 * Model for a bad request error response with detailed field errors.
 */
public class BadRequestErrorResponse {
    private String message;
    private List<Detail> details;

    /**
     * Default constructor.
     */
    public BadRequestErrorResponse() {}

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

    /**
     * Gets the list of error details.
     * @return the list of details
     */
    public List<Detail> getDetails() {
        return details;
    }

    /**
     * Sets the list of error details.
     * @param details the list of details
     */
    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    /**
     * Model for a single field error detail in a bad request response.
     */
    public static class Detail {
        private String field;
        private String message;
        private String type;

        /**
         * Default constructor.
         */
        public Detail() {}

        /**
         * Constructor with all fields.
         * @param field the field name
         * @param message the error message
         * @param type the error type
         */
        public Detail(String field, String message, String type) {
            this.field = field;
            this.message = message;
            this.type = type;
        }

        /**
         * Gets the field name with the error.
         * @return the field name
         */
        public String getField() {
            return field;
        }

        /**
         * Sets the field name with the error.
         * @param field the field name
         */
        public void setField(String field) {
            this.field = field;
        }

        /**
         * Gets the error message for the field.
         * @return the error message
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets the error message for the field.
         * @param message the error message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * Gets the type of error (e.g., missing, pattern).
         * @return the error type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the type of error (e.g., missing, pattern).
         * @param type the error type
         */
        public void setType(String type) {
            this.type = type;
        }
    }
} 