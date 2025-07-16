package org.banking.service.service;

import org.banking.service.model.*;

public interface TransactionService {
    TransactionResponse createTransaction(String accountNumber, CreateTransactionRequest request);
    ListTransactionsResponse listTransactions(String accountNumber);
    TransactionResponse fetchTransaction(String accountNumber, String transactionId);
} 