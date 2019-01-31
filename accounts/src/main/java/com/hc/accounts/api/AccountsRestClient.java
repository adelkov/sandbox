package com.hc.accounts.api;

import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class AccountsRestClient implements AccountsClient {
    private WebClient webClient;

    public AccountsRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public AsyncResult<String> addUser(UserModel user) {
        return Future.future(fut -> {
            this.webClient
                    .post("/add-user")
                    .sendJsonObject(JsonObject.mapFrom(user), res -> {
                        if (res.succeeded()) {
                            System.out.println(res.result().body());
                            fut.complete(res.result().body().toString());
                        } else {
                            fut.fail(res.cause());
                        }
                    });
        });
    }

    @Override
    public AsyncResult<Integer> withDraw(WithdrawRequest withdrawRequest) {
        return Future.future(fut -> {
            this.webClient
                    .post("/withdraw")
                    .sendJsonObject(JsonObject.mapFrom(withdrawRequest), res -> {
                        if (res.succeeded()) {
                            System.out.println(res.result().body());
                            fut.complete(Integer.parseInt(res.result().body().toString()));
                        } else {
                            fut.fail(res.cause());
                        }
                    });
        });
    }

    @Override
    public AsyncResult<Integer> deposit(DepositRequest depositRequest) {
        return Future.future(fut -> {
            this.webClient
                    .post("/deposit")
                    .sendJsonObject(JsonObject.mapFrom(depositRequest), res -> {
                        if (res.succeeded()) {
                            System.out.println(res.result().body());
                            fut.complete(Integer.parseInt(res.result().body().toString()));
                        } else {
                            fut.fail(res.cause());
                        }
                    });
        });
    }
}
