package org.banking.service.controller;

import org.banking.service.model.*;
import org.banking.service.service.TransactionService;
import org.banking.service.util.ValidationUtil;
import org.banking.service.service.AccountService;
import org.banking.service.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * REST controller for transaction management endpoints.
 * Provides endpoints to create, fetch, and list transactions for a bank account.
 */
@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    /**
     * Creates a new transaction (deposit or withdrawal) for a bank account.
     * @param authorization the Authorization header
     * @param accountNumber the account number
     * @param request the transaction creation request
     * @return the created transaction response
     */
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestHeader(value = "Authorization", required = false) String authorization,
                                               @PathVariable String accountNumber, @RequestBody CreateTransactionRequest request) {
        try {
            ValidationUtil.validateBearerToken(authorization);
            ValidationUtil.validateAccountNumber(accountNumber);
            String token = authorization.substring(7);
            String userId = org.banking.service.util.JwtUtil.validateTokenAndGetSubject(token);
            BankAccountResponse account = accountService.fetchAccount(accountNumber);
            if (account == null) {
                ErrorResponse error = ErrorUtil.error("Bank account was not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            if (!userId.equals(account.getUserId())) {
                ErrorResponse error = ErrorUtil.error("Forbidden: You can only transact on your own bank account");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }
            List<BadRequestErrorResponse.Detail> details = ValidationUtil.validateCreateTransactionRequestAll(request);
            if (!details.isEmpty()) {
                return new ResponseEntity<>(ErrorUtil.badRequest("Invalid details supplied", details), HttpStatus.BAD_REQUEST);
            }
            Double amount = request.getAmount();
            if ("withdrawal".equalsIgnoreCase(request.getType())) {
                if (amount == null || amount > account.getBalance()) {
                    ErrorResponse error = ErrorUtil.error("Insufficient funds to process transaction");
                    return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                account.setBalance(account.getBalance() - amount);
                account.setUpdatedTimestamp(OffsetDateTime.now());
            } else if ("deposit".equalsIgnoreCase(request.getType())) {
                account.setBalance(account.getBalance() + amount);
                account.setUpdatedTimestamp(OffsetDateTime.now());
            }
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if (msg.contains("Authorization")) {
                ErrorResponse error = ErrorUtil.error("Access token is missing or invalid");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            } else {
                String field = null;
                if (msg.contains("accountNumber")) field = "accountNumber";
                else if (msg.contains("transactionId")) field = "transactionId";
                else if (msg.contains("amount")) field = "amount";
                else if (msg.contains("currency")) field = "currency";
                else if (msg.contains("type")) field = "type";
                BadRequestErrorResponse error = ErrorUtil.badRequest(
                    msg,
                    List.of(ErrorUtil.detail(field, msg, "pattern"))
                );
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        }
        TransactionResponse response = transactionService.createTransaction(accountNumber, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Lists all transactions for a bank account.
     * @param authorization the Authorization header
     * @param accountNumber the account number
     * @return the list of transactions response
     */
    @GetMapping
    public ResponseEntity<?> listTransactions(@RequestHeader(value = "Authorization", required = false) String authorization,
                                              @PathVariable String accountNumber) {
        try {
            ValidationUtil.validateBearerToken(authorization);
            ValidationUtil.validateAccountNumber(accountNumber);
            String token = authorization.substring(7);
            String userId = org.banking.service.util.JwtUtil.validateTokenAndGetSubject(token);
            BankAccountResponse account = accountService.fetchAccount(accountNumber);
            if (account == null) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Bank account was not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            if (!userId.equals(account.getUserId())) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Forbidden: You can only view transactions for your own bank account");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if (msg.contains("Authorization")) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Access token is missing or invalid");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            } else {
                String field = null;
                if (msg.contains("accountNumber")) field = "accountNumber";
                else if (msg.contains("transactionId")) field = "transactionId";
                else if (msg.contains("amount")) field = "amount";
                else if (msg.contains("currency")) field = "currency";
                else if (msg.contains("type")) field = "type";
                BadRequestErrorResponse error = ErrorUtil.badRequest(
                    msg,
                    java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
                );
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        }
        ListTransactionsResponse response = transactionService.listTransactions(accountNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetches a transaction by account number and transaction ID.
     * @param authorization the Authorization header
     * @param accountNumber the account number
     * @param transactionId the transaction ID
     * @return the transaction response
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<?> fetchTransaction(@RequestHeader(value = "Authorization", required = false) String authorization,
                                              @PathVariable String accountNumber, @PathVariable String transactionId) {
        try {
            ValidationUtil.validateBearerToken(authorization);
            ValidationUtil.validateAccountNumber(accountNumber);
            ValidationUtil.validateTransactionId(transactionId);
            String token = authorization.substring(7);
            String userId = org.banking.service.util.JwtUtil.validateTokenAndGetSubject(token);
            BankAccountResponse account = accountService.fetchAccount(accountNumber);
            if (account == null) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Bank account was not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            if (!userId.equals(account.getUserId())) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Forbidden: You can only view transactions for your own bank account");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if (msg.contains("Authorization")) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Access token is missing or invalid");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            } else {
                String field = null;
                if (msg.contains("accountNumber")) field = "accountNumber";
                else if (msg.contains("transactionId")) field = "transactionId";
                else if (msg.contains("amount")) field = "amount";
                else if (msg.contains("currency")) field = "currency";
                else if (msg.contains("type")) field = "type";
                BadRequestErrorResponse error = ErrorUtil.badRequest(
                    msg,
                    java.util.List.of(ErrorUtil.detail(field, msg, "pattern"))
                );
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        }
        TransactionResponse response = transactionService.fetchTransaction(accountNumber, transactionId);
        if (response == null) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Transaction was not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }
}
