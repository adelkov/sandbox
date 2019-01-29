package com.hc.accounts.core.data;

import java.math.BigDecimal;

public class UserModel {
    private String userName;
    private String userId;
    private int balance;

    public UserModel(String userName, String userId, int balance) {
        this.userName = userName;
        this.userId = userId;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
