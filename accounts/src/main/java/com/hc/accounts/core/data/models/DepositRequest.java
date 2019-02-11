package com.hc.accounts.core.data.models;

public class DepositRequest {
    public String userId;
    public int amount;

    public DepositRequest() {
    }

    public DepositRequest(String userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
