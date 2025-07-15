package org.banking.service.model;

import java.time.OffsetDateTime;

/**
 * Response model for a transaction, including details and metadata.
 */
public class TransactionResponse {
    private String id;
    private double amount;
    private String currency;
    private String type;
    private String reference;
    private String userId;
    private OffsetDateTime createdTimestamp;

    /**
     * Default constructor.
     */
    public TransactionResponse() {}

    /**
     * Gets the transaction ID.
     * @return the transaction ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the transaction ID.
     * @param id the transaction ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the transaction amount.
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the transaction amount.
     * @param amount the amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the transaction currency.
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the transaction currency.
     * @param currency the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the transaction type (deposit or withdrawal).
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the transaction type (deposit or withdrawal).
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the transaction reference.
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the transaction reference.
     * @param reference the reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Gets the user ID associated with the transaction.
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the transaction.
     * @param userId the user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the timestamp when the transaction was created.
     * @return the created timestamp
     */
    public OffsetDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * Sets the timestamp when the transaction was created.
     * @param createdTimestamp the created timestamp
     */
    public void setCreatedTimestamp(OffsetDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
} 