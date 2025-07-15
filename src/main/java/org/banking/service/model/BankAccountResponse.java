package org.banking.service.model;

import java.time.OffsetDateTime;

/**
 * Response model for a bank account, including account details and metadata.
 */
public class BankAccountResponse {
    private String accountNumber;
    private String sortCode;
    private String name;
    private String accountType;
    private double balance;
    private String currency;
    private OffsetDateTime createdTimestamp;
    private OffsetDateTime updatedTimestamp;
    private String userId;

    /**
     * Default constructor.
     */
    public BankAccountResponse() {}

    /**
     * Gets the account number.
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number.
     * @param accountNumber the account number
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the sort code.
     * @return the sort code
     */
    public String getSortCode() {
        return sortCode;
    }

    /**
     * Sets the sort code.
     * @param sortCode the sort code
     */
    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

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

    /**
     * Gets the account balance.
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the account balance.
     * @param balance the balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the account currency.
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the account currency.
     * @param currency the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the timestamp when the account was created.
     * @return the created timestamp
     */
    public OffsetDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * Sets the timestamp when the account was created.
     * @param createdTimestamp the created timestamp
     */
    public void setCreatedTimestamp(OffsetDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    /**
     * Gets the timestamp when the account was last updated.
     * @return the updated timestamp
     */
    public OffsetDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    /**
     * Sets the timestamp when the account was last updated.
     * @param updatedTimestamp the updated timestamp
     */
    public void setUpdatedTimestamp(OffsetDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    /**
     * Gets the user ID of the account owner.
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of the account owner.
     * @param userId the user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
} 