package org.banking.service.model;

/**
 * Request model for creating a new bank account.
 */
public class CreateBankAccountRequest {
    private String name;
    private String accountType;

    /**
     * Default constructor.
     */
    public CreateBankAccountRequest() {}

    /**
     * Gets the account name.
     * @return the account name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the account name.
     * @param name the account name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the account type.
     * @return the account type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the account type.
     * @param accountType the account type
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
} 