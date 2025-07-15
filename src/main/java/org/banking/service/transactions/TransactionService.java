package org.banking.service.transactions;

import org.banking.service.model.*;

public interface TransactionService {
    TransactionResponse createTransaction(String accountNumber, CreateTransactionRequest request);
    ListTransactionsResponse listTransactions(String accountNumber);
    TransactionResponse fetchTransaction(String accountNumber, String transactionId);
} 