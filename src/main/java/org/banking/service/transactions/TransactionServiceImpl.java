package org.banking.service.transactions;

import org.banking.service.model.*;
import org.banking.service.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.Collections;

/**
 * Service implementation for transaction management.
 * Stores transactions in a thread-safe in-memory map.
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    private final Map<String, List<TransactionResponse>> transactions = new ConcurrentHashMap<>();

    /**
     * Creates a new transaction for the given account number.
     * @param accountNumber the account number
     * @param request the transaction creation request
     * @return the created transaction response
     */
    @Override
    public TransactionResponse createTransaction(String accountNumber, CreateTransactionRequest request) {
        TransactionResponse transaction = new TransactionResponse();
        transaction.setId(generateTransactionId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setType(request.getType());
        transaction.setReference(request.getReference());
        transaction.setCreatedTimestamp(OffsetDateTime.now());
        transactions.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(transaction);
        return transaction;
    }

    /**
     * Lists all transactions for the given account number.
     * @param accountNumber the account number
     * @return the list of transactions response
     */
    @Override
    public ListTransactionsResponse listTransactions(String accountNumber) {
        ListTransactionsResponse response = new ListTransactionsResponse();
        response.setTransactions(transactions.getOrDefault(accountNumber, Collections.emptyList()));
        return response;
    }

    /**
     * Fetches a transaction by account number and transaction ID.
     * @param accountNumber the account number
     * @param transactionId the transaction ID
     * @return the transaction response, or null if not found
     */
    @Override
    public TransactionResponse fetchTransaction(String accountNumber, String transactionId) {
        List<TransactionResponse> txs = transactions.getOrDefault(accountNumber, Collections.emptyList());
        return txs.stream().filter(tx -> tx.getId().equals(transactionId)).findFirst().orElse(null);
    }

    /**
     * Generates a unique transaction ID.
     * @return the generated transaction ID
     */
    private String generateTransactionId() {
        return "tan-" + UUID.randomUUID().toString().replace("-", "").substring(0, 7);
    }
} 