package org.banking.service.controller;

import org.banking.service.model.*;
import org.banking.service.service.AccountService;
import org.banking.service.util.ErrorUtil;
import org.banking.service.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for account management endpoints.
 * Provides endpoints to create, fetch, update, and list bank accounts.
 */
@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    /**
     * Constructs an AccountController with the given account service.
     * @param accountService the account service
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new bank account for a user.
     * @param request the account creation request
     * @return the created bank account response
     */
    @PostMapping("")
    public ResponseEntity<?> createAccount(@RequestHeader("Authorization") String authHeader, @RequestBody CreateBankAccountRequest request) {
        // Extract userId from JWT
        String userId = org.banking.service.util.JwtUtil.validateTokenAndGetSubject(authHeader.substring(7));
        try {
            ValidationUtil.validateUserId(userId);
            java.util.List<BadRequestErrorResponse.Detail> details = ValidationUtil.validateCreateBankAccountRequestAll(request);
            if (!details.isEmpty()) {
                String topMessage = details.size() == 1 ? details.get(0).getMessage() : "Invalid details supplied";
                BadRequestErrorResponse error = new BadRequestErrorResponse();
                error.setMessage(topMessage);
                error.setDetails(details);
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            BankAccountResponse account = accountService.createAccount(userId, request);
            return new ResponseEntity<>(account, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            String field = null;
            if (msg.contains("accountNumber")) field = "accountNumber";
            else if (msg.contains("name")) field = "name";
            else if (msg.contains("accountType")) field = "accountType";
            BadRequestErrorResponse error = ErrorUtil.badRequest(
                msg,
                java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Lists all bank accounts for the authenticated user.
     * @return the list of bank accounts response
     */
    @GetMapping("")
    public ResponseEntity<?> listAccounts(@RequestHeader("Authorization") String authHeader) {
        String userId = org.banking.service.util.JwtUtil.validateTokenAndGetSubject(authHeader.substring(7));
        ListBankAccountsResponse response = accountService.listAccounts(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Fetches a bank account by account number.
     * @param accountNumber the account number
     * @param authHeader the Authorization header
     * @return the bank account response
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> fetchAccount(@PathVariable String accountNumber, @RequestHeader("Authorization") String authHeader) {
        try {
            ValidationUtil.validateAccountNumber(accountNumber);
            ValidationUtil.validateAuthHeaderForAccount(authHeader, accountService.fetchAccount(accountNumber));
            BankAccountResponse account = accountService.fetchAccount(accountNumber);
            if (account == null) {
                return new ResponseEntity<>(ErrorUtil.error("Account not found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            String field = null;
            if (msg.contains("accountNumber")) field = "accountNumber";
            else if (msg.contains("name")) field = "name";
            else if (msg.contains("accountType")) field = "accountType";
            BadRequestErrorResponse error = ErrorUtil.badRequest(
                msg,
                java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing bank account.
     * @param accountNumber the account number
     * @param request the update request
     * @return the updated bank account response
     */
    @PatchMapping("/{accountNumber}")
    public ResponseEntity<?> updateAccount(@PathVariable String accountNumber, @RequestBody UpdateBankAccountRequest request) {
        try {
            ValidationUtil.validateAccountNumber(accountNumber);
            ValidationUtil.validateUpdateBankAccountRequest(request);
            BankAccountResponse account = accountService.updateAccount(accountNumber, request);
            if (account == null) {
                return new ResponseEntity<>(ErrorUtil.error("Account not found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            String field = null;
            if (msg.contains("accountNumber")) field = "accountNumber";
            else if (msg.contains("name")) field = "name";
            else if (msg.contains("accountType")) field = "accountType";
            BadRequestErrorResponse error = ErrorUtil.badRequest(
                msg,
                java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a bank account by account number if owned by the authenticated user.
     * @param accountNumber the account number
     * @param authHeader the Authorization header
     * @return 204 No Content if deleted, 404 if not found, 403 if forbidden, 400 if invalid
     */
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountNumber, @RequestHeader("Authorization") String authHeader) {
        try {
            ValidationUtil.validateAccountNumber(accountNumber);
            BankAccountResponse account = accountService.fetchAccount(accountNumber);
            ValidationUtil.validateAuthHeaderForAccount(authHeader, account);
            if (account == null) {
                return new ResponseEntity<>(ErrorUtil.error("Account not found"), HttpStatus.NOT_FOUND);
            }
            accountService.deleteAccount(accountNumber);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if ("Account not found".equals(msg)) {
                return new ResponseEntity<>(ErrorUtil.error(msg), HttpStatus.NOT_FOUND);
            } else if ("Forbidden: You can only access your own bank account".equals(msg)) {
                return new ResponseEntity<>(ErrorUtil.error(msg), HttpStatus.FORBIDDEN);
            }
            String field = null;
            if (msg.contains("accountNumber")) field = "accountNumber";
            BadRequestErrorResponse error = ErrorUtil.badRequest(
                msg,
                java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
