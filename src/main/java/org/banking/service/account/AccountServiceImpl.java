package org.banking.service.account;

import org.banking.service.model.*;
import org.banking.service.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service implementation for account management.
 * Stores bank accounts in a thread-safe in-memory map.
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final Map<String, BankAccountResponse> accounts = new ConcurrentHashMap<>();
    private static final String SORT_CODE = "10-10-10";
    private static final String CURRENCY = "GBP";

    /**
     * Creates a new bank account for the given user.
     * @param userId the user ID
     * @param request the account creation request
     * @return the created bank account response
     */
    @Override
    public BankAccountResponse createAccount(String userId, CreateBankAccountRequest request) {
        String accountNumber = generateAccountNumber();
        OffsetDateTime now = OffsetDateTime.now();
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber(accountNumber);
        account.setSortCode(SORT_CODE);
        account.setName(request.getName());
        account.setAccountType(request.getAccountType());
        account.setBalance(0.0);
        account.setCurrency(CURRENCY);
        account.setCreatedTimestamp(now);
        account.setUpdatedTimestamp(now);
        account.setUserId(userId);
        accounts.put(accountNumber, account);
        return account;
    }

    /**
     * Lists all bank accounts for the given user.
     * @param userId the user ID
     * @return the list of bank accounts response
     */
    @Override
    public ListBankAccountsResponse listAccounts(String userId) {
        ListBankAccountsResponse response = new ListBankAccountsResponse();
        List<BankAccountResponse> filtered = new ArrayList<>();
        for (BankAccountResponse acc : accounts.values()) {
            if (userId.equals(acc.getUserId())) {
                filtered.add(acc);
            }
        }
        response.setAccounts(filtered);
        return response;
    }

    /**
     * Fetches a bank account by account number.
     * @param accountNumber the account number
     * @return the bank account response, or null if not found
     */
    @Override
    public BankAccountResponse fetchAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Updates an existing bank account with new data.
     * @param accountNumber the account number
     * @param request the update request
     * @return the updated bank account response, or null if not found
     */
    @Override
    public BankAccountResponse updateAccount(String accountNumber, UpdateBankAccountRequest request) {
        BankAccountResponse account = accounts.get(accountNumber);
        if (account != null) {
            if (request.getName() != null) account.setName(request.getName());
            if (request.getAccountType() != null) account.setAccountType(request.getAccountType());
            account.setUpdatedTimestamp(OffsetDateTime.now());
        }
        return account;
    }

    /**
     * Deletes a bank account by account number.
     * @param accountNumber the account number
     */
    @Override
    public void deleteAccount(String accountNumber) {
        accounts.remove(accountNumber);
    }

    /**
     * Generates a unique account number.
     * @return the generated account number
     */
    private String generateAccountNumber() {
        return "0100000" + (accounts.size() + 1);
    }
} 