package com.hc.accounts.core;

import com.hc.accounts.api.AccountsEventBusClient;
import com.hc.accounts.core.data.UserDAOMemory;
import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class RestAPI extends AbstractVerticle {

    private AccountsEventBusClient accountsEventBusClient;

    @Override
    public void start(Future<Void> future) {
        this.accountsEventBusClient = new AccountsEventBusClient(vertx.eventBus());

        Router router = Router.router(vertx);

        router.post("/add-user")
                .handler(BodyHandler.create())
                .handler(this::addUser);

        router.post("/withdraw")
                .handler(BodyHandler.create())
                .handler(this::withdraw);

        router.post("/deposit")
                .handler(BodyHandler.create())
                .handler(this::deposit);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.failed();
                    }
                });
    }

    private void deposit(RoutingContext routingContext) {
        DepositRequest depositRequest = routingContext.getBodyAsJson().mapTo(DepositRequest.class);
        this.accountsEventBusClient.deposit(depositRequest)
                .map(res -> {
                    routingContext
                            .response()
                            .putHeader("content-type", HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .end(res.toString());
                    return res;
                })
                .otherwise(error -> {
                    routingContext
                            .response()
                            .putHeader("content-type", HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(error.getMessage());
                    return null;
                });
    }

    private void withdraw(RoutingContext routingContext) {
        WithdrawRequest withdrawRequest = routingContext.getBodyAsJson().mapTo(WithdrawRequest.class);
        this.accountsEventBusClient.withDraw(withdrawRequest)
                .map(res -> {
                    routingContext
                            .response()
                            .putHeader("content-type", HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .end(res.toString());
                    return res;
                })
                .otherwise(error -> {
                    routingContext
                            .response()
                            .putHeader("content-type", HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(error.getMessage());
                    return null;
                });
    }


    private void addUser(RoutingContext routingContext) {
        UserModel userModel = routingContext.getBodyAsJson().mapTo(UserModel.class);
        this.accountsEventBusClient.addUser(userModel)
                .map(res -> {
                    routingContext
                            .response()
                            .putHeader("content-type", HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .end(res);
                    return res;
                })
                .otherwise(error -> {
                    routingContext
                            .response()
                            .putHeader("content-type", HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(error.getMessage());
                    return null;
                });
    }
}
