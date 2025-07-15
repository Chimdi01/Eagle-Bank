package org.banking.service.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating user, account, and transaction data formats.
 */
public class ValidationUtil {
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^01\\d{6}$");
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^usr-[A-Za-z0-9]+$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+[1-9]\\d{1,14}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern TRANSACTION_ID_PATTERN = Pattern.compile("^tan-[A-Za-z0-9]+$");

    /**
     * Validates the format of an account number.
     * @param accountNumber the account number to validate
     * @throws IllegalArgumentException if the format is invalid
     */
    public static void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || !ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches()) {
            throw new IllegalArgumentException("Invalid accountNumber format");
        }
    }

    /**
     * Validates the format of a user ID.
     * @param userId the user ID to validate
     * @throws IllegalArgumentException if the format is invalid
     */
    public static void validateUserId(String userId) {
        if (userId == null || !USER_ID_PATTERN.matcher(userId).matches()) {
            throw new IllegalArgumentException("Invalid userId format");
        }
    }

    /**
     * Validates the format of a phone number.
     * @param phoneNumber the phone number to validate
     * @throws IllegalArgumentException if the format is invalid
     */
    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Invalid phoneNumber format");
        }
    }

    /**
     * Validates the format of an email address.
     * @param email the email address to validate
     * @throws IllegalArgumentException if the format is invalid
     */
    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Validates the format of a transaction ID.
     * @param transactionId the transaction ID to validate
     * @throws IllegalArgumentException if the format is invalid
     */
    public static void validateTransactionId(String transactionId) {
        if (transactionId == null || !TRANSACTION_ID_PATTERN.matcher(transactionId).matches()) {
            throw new IllegalArgumentException("Invalid transactionId format");
        }
    }

    /**
     * Validates the format of a Bearer token in the Authorization header.
     * @param authorizationHeader the Authorization header value
     * @throws IllegalArgumentException if the header is missing or invalid
     */
    public static void validateBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || authorizationHeader.length() <= 7) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
    }

    /**
     * Validates a CreateUserRequest for required fields and correct formats.
     * @param request the CreateUserRequest to validate
     * @throws IllegalArgumentException if any required field is missing or invalid
     */
    public static void validateCreateUserRequest(org.banking.service.model.CreateUserRequest request) {
        if (request == null) throw new IllegalArgumentException("Request body is missing");
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: name");
        }
        if (request.getAddress() == null) {
            throw new IllegalArgumentException("Missing required field: address");
        }
        org.banking.service.model.CreateUserRequest.Address addr = request.getAddress();
        if (addr.getLine1() == null || addr.getLine1().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: address.line1");
        }
        if (addr.getTown() == null || addr.getTown().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: address.town");
        }
        if (addr.getCounty() == null || addr.getCounty().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: address.county");
        }
        if (addr.getPostcode() == null || addr.getPostcode().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: address.postcode");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: phoneNumber");
        }
        validatePhoneNumber(request.getPhoneNumber());
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: email");
        }
        validateEmail(request.getEmail());
    }

    /**
     * Validates an UpdateUserRequest for correct formats if fields are present.
     * @param request the UpdateUserRequest to validate
     * @throws IllegalArgumentException if any present field is invalid
     */
    public static void validateUpdateUserRequest(org.banking.service.model.UpdateUserRequest request) {
        if (request == null) throw new IllegalArgumentException("Request body is missing");
        if (request.getPhoneNumber() != null) validatePhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) validateEmail(request.getEmail());
        // Optionally, validate address fields if present
        if (request.getAddress() != null) {
            org.banking.service.model.UpdateUserRequest.Address addr = request.getAddress();
            if (addr.getLine1() != null && addr.getLine1().trim().isEmpty()) {
                throw new IllegalArgumentException("address.line1 cannot be empty");
            }
            if (addr.getTown() != null && addr.getTown().trim().isEmpty()) {
                throw new IllegalArgumentException("address.town cannot be empty");
            }
            if (addr.getCounty() != null && addr.getCounty().trim().isEmpty()) {
                throw new IllegalArgumentException("address.county cannot be empty");
            }
            if (addr.getPostcode() != null && addr.getPostcode().trim().isEmpty()) {
                throw new IllegalArgumentException("address.postcode cannot be empty");
            }
        }
    }

    /**
     * Validates the Authorization header and ensures the JWT subject matches the userId.
     * @param authHeader the Authorization header
     * @param userId the user ID to match
     * @throws IllegalArgumentException if the header is missing/invalid or subject does not match userId
     */
    public static void validateAuthHeader(String authHeader, String userId) {
        validateBearerToken(authHeader);
        String token = authHeader.substring(7);
        String subject = org.banking.service.util.JwtUtil.validateTokenAndGetSubject(token);
        if (!userId.equals(subject)) {
            throw new IllegalArgumentException("Forbidden: You can only access your own user details");
        }
    }

    /**
     * Validates the Authorization header and ensures the JWT subject matches the account's userId.
     * @param authHeader the Authorization header
     * @param account the BankAccountResponse to check ownership
     * @throws IllegalArgumentException if the header is missing/invalid or subject does not match account userId
     */
    public static void validateAuthHeaderForAccount(String authHeader, org.banking.service.model.BankAccountResponse account) {
        validateBearerToken(authHeader);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        String token = authHeader.substring(7);
        String subject = org.banking.service.util.JwtUtil.validateTokenAndGetSubject(token);
        if (!subject.equals(account.getUserId())) {
            throw new IllegalArgumentException("Forbidden: You can only access your own bank account");
        }
    }

    /**
     * Validates an UpdateBankAccountRequest for correct formats if fields are present.
     * @param request the UpdateBankAccountRequest to validate
     * @throws IllegalArgumentException if any present field is invalid
     */
    public static void validateUpdateBankAccountRequest(org.banking.service.model.UpdateBankAccountRequest request) {
        if (request == null) throw new IllegalArgumentException("Request body is missing");
        if (request.getName() != null && request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (request.getAccountType() != null && request.getAccountType().trim().isEmpty()) {
            throw new IllegalArgumentException("accountType cannot be empty");
        }
    }

    /**
     * Validates a CreateBankAccountRequest for required fields and returns a list of error details for all missing fields.
     * @param request the CreateBankAccountRequest to validate
     * @return a list of BadRequestErrorResponse.Detail for all missing fields
     */
    public static java.util.List<org.banking.service.model.BadRequestErrorResponse.Detail> validateCreateBankAccountRequestAll(org.banking.service.model.CreateBankAccountRequest request) {
        java.util.List<org.banking.service.model.BadRequestErrorResponse.Detail> details = new java.util.ArrayList<>();
        if (request == null) {
            details.add(new org.banking.service.model.BadRequestErrorResponse.Detail("request", "Request body is missing", "pattern"));
            return details;
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            details.add(new org.banking.service.model.BadRequestErrorResponse.Detail("name", "Missing required field: name", "pattern"));
        }
        if (request.getAccountType() == null || request.getAccountType().trim().isEmpty()) {
            details.add(new org.banking.service.model.BadRequestErrorResponse.Detail("accountType", "Missing required field: accountType", "pattern"));
        }
        return details;
    }
} 