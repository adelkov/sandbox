package com.hc.accounts.api;

import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class AccountsEventBusClient implements AccountsClient {

    private EventBus eventBus;

    public AccountsEventBusClient(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public AsyncResult<String> addUser(UserModel user) {
        JsonObject userJsonObject = JsonObject.mapFrom(user);
        return Future.future(fut -> {
            this.eventBus.send("ADD_USER", userJsonObject, res -> {
                if (res.succeeded()) {
                    fut.complete(res.result().body().toString());
                } else {
                    fut.fail(res.cause());
                }
            });
        });
    }

    @Override
    public AsyncResult<Integer> withDraw(WithdrawRequest withdrawRequest) {
        JsonObject withdrawRequestJsonObject = JsonObject.mapFrom(withdrawRequest);

        return Future.future(fut -> {
            this.eventBus.send("WITHDRAW", withdrawRequestJsonObject, res -> {
                if (res.succeeded()) {
                    fut.complete(Integer.parseInt(res.result().body().toString()));
                } else {
                    fut.fail(res.cause());
                }
            });
        });
    }

    @Override
    public AsyncResult<Integer> deposit(DepositRequest depositRequest) {
        JsonObject depositRequestJsonObject = JsonObject.mapFrom(depositRequest);
        return Future.future(fut -> {
            this.eventBus.send("DEPOSIT", depositRequestJsonObject, res -> {
                if (res.succeeded()) {
                    fut.complete(Integer.parseInt(res.result().body().toString()));
                } else {
                    fut.fail(res.cause());
                }
            });
        });
    }
}
