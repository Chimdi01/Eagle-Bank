package org.banking.service.model;

public class UpdateBankAccountRequest {
    private String name;
    private String accountType;

    public UpdateBankAccountRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
} 