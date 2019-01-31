package com.hc.accounts.api;

import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AsyncResult;

public interface AccountsClient {

    AsyncResult<String> addUser(UserModel user);
    AsyncResult<Integer> withDraw(WithdrawRequest withdrawRequest);
    AsyncResult<Integer> deposit(DepositRequest depositRequest);

}
