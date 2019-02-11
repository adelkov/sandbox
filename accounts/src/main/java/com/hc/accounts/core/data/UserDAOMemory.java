package com.hc.accounts.core.data;

import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;

import java.util.HashMap;
import java.util.Map;

public class UserDAOMemory implements UserDAOInterface {
    private Map<String, UserModel> users;

    public UserDAOMemory() {
        this.users = new HashMap<>();
        users.put("AK2018/1", new UserModel("Kovács Adél", "AK2018/1", 24234));
        users.put("AK2018/2", new UserModel("Kutya Cica", "AK2018/2", 23));
        users.put("AK2018/3", new UserModel("Béla Alma", "AK2018/3", -10));
    }

    public AsyncResult<Map<String, UserModel>> getUsers() {
        return Future.future(fut -> {
            fut.complete(this.users);
        });
    }

    public AsyncResult<UserModel> addUser(UserModel user) {
        return Future.future(fut -> {
            this.users.put(user.getUserId(), user);
            fut.complete(user);
        });
    }

    public AsyncResult<UserModel> getUserWithUserId(String id) {
        return Future.future(fut -> {
            fut.complete(this.users.get(id));
        });
    }

    public AsyncResult<Integer> deposit(DepositRequest request) {
        return Future.future(fut -> {
            UserModel userModel = this.users.get(request.userId);
            userModel.deposit(request.amount);
            fut.complete(userModel.getBalance());
        });
    }

    public AsyncResult<Integer> withdraw(WithdrawRequest request) {
        return Future.future(fut -> {
            UserModel userModel = this.users.get(request.userId);
            userModel.withdraw(request.amount);
            fut.complete(userModel.getBalance());
        });
    }
}
