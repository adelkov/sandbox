package com.hc.accounts.core.data.models;

public class WithdrawRequest {
    public String userId;
    public int amount;

    public WithdrawRequest() {
    }

    public WithdrawRequest(String userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
