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
}
