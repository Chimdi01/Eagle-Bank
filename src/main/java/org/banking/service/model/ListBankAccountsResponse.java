package org.banking.service.model;

import java.util.List;

/**
 * Response model for listing multiple bank accounts.
 */
public class ListBankAccountsResponse {
    private List<BankAccountResponse> accounts;

    /**
     * Default constructor.
     */
    public ListBankAccountsResponse() {}

    /**
     * Gets the list of bank accounts.
     * @return the list of accounts
     */
    public List<BankAccountResponse> getAccounts() {
        return accounts;
    }

    /**
     * Sets the list of bank accounts.
     * @param accounts the list of accounts
     */
    public void setAccounts(List<BankAccountResponse> accounts) {
        this.accounts = accounts;
    }
} 