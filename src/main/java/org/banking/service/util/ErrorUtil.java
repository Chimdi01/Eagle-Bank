package org.banking.service.util;

import org.banking.service.model.BadRequestErrorResponse;
import org.banking.service.model.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for constructing error responses for the API.
 */
public class ErrorUtil {
    /**
     * Creates a simple error response with a message.
     * @param message the error message
     * @return the error response
     */
    public static ErrorResponse error(String message) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(message);
        return error;
    }

    /**
     * Creates a bad request error response with details.
     * @param message the error message
     * @param details the list of error details
     * @return the bad request error response
     */
    public static BadRequestErrorResponse badRequest(String message, List<BadRequestErrorResponse.Detail> details) {
        BadRequestErrorResponse error = new BadRequestErrorResponse();
        error.setMessage(message);
        error.setDetails(details);
        return error;
    }

    /**
     * Creates a detail object for a bad request error.
     * @param field the field with the error
     * @param message the error message
     * @param type the type of error (e.g., missing, pattern)
     * @return the detail object
     */
    public static BadRequestErrorResponse.Detail detail(String field, String message, String type) {
        BadRequestErrorResponse.Detail detail = new BadRequestErrorResponse.Detail();
        detail.setField(field);
        detail.setMessage(message);
        detail.setType(type);
        return detail;
    }

    /**
     * Creates a new list for error details.
     * @return a new list of error details
     */
    public static List<BadRequestErrorResponse.Detail> details() {
        return new ArrayList<>();
    }
} 