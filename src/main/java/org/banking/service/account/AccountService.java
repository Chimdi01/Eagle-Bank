package org.banking.service.account;

import org.banking.service.model.*;

public interface AccountService {
    BankAccountResponse createAccount(String userId, CreateBankAccountRequest request);
    ListBankAccountsResponse listAccounts(String userId);
    BankAccountResponse fetchAccount(String accountNumber);
    BankAccountResponse updateAccount(String accountNumber, UpdateBankAccountRequest request);
    void deleteAccount(String accountNumber);
} 