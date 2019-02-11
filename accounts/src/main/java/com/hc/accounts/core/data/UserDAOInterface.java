package com.hc.accounts.core.data;

import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AsyncResult;

import java.util.Map;

interface UserDAOInterface {
    AsyncResult<UserModel> getUserWithUserId(String userId);

    AsyncResult<UserModel> addUser(UserModel user);

    AsyncResult<Map<String, UserModel>> getUsers();

    AsyncResult<Integer> withdraw(WithdrawRequest request);

    AsyncResult<Integer> deposit(DepositRequest request);
}
