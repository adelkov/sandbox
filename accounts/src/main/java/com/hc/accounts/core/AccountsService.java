package com.hc.accounts.core;

import com.hc.accounts.core.data.UserDAODatabase;
import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class AccountsService extends AbstractVerticle {

    private UserDAODatabase userDAO;

    public AccountsService(UserDAODatabase userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer("ADD_USER", this::addUser);
        vertx.eventBus().consumer("DEPOSIT", this::deposit);
        vertx.eventBus().consumer("WITHDRAW", this::withdraw);
    }

    private void addUser(final Message<JsonObject> message) {
        UserModel user = message.body().mapTo(UserModel.class);

        if (user.getUserName() == null) {
            message.fail(-404, "missing userName");
        }

        if (user.getUserId() == null) {
            message.fail(-404, "missing userId");
        }

        userDAO.addUser(user)
                .map(res -> {
                    message.reply(res.getUserId());
                    return res;
                })
                .otherwise(error -> {
                    message.fail(-500, "server error");
                    return null;
                });
    }

    private void deposit(final Message<JsonObject> message) {
        if (message.body().getString("userId") == null) {
            message.fail(-404, "missing userId");
            return;
        }

        if (message.body().getInteger("amount") == null) {
            message.fail(-404, "missing amount");
            return;
        }

        if (message.body().getInteger("amount") < 0) {
            message.fail(-404, "negative amount");
        }

        userDAO.deposit(message.body().mapTo(DepositRequest.class))
                .map(res -> {
                    message.reply(res);
                    return res;
                })
                .otherwise(error -> {
                    message.fail(-500, "server error");
                    return null;
                });
    }


    private void withdraw(final Message<JsonObject> message) {
        if (message.body().getString("userId") == null) {
            message.fail(-404, "missing userId");
            return;
        }

        if (message.body().getInteger("amount") == null) {
            message.fail(-404, "missing amount");
            return;
        }

        if (message.body().getInteger("amount") < 0) {
            message.fail(-404, "negative amount");
        }

        userDAO.withdraw(message.body().mapTo(WithdrawRequest.class))
                .map(res -> {
                    message.reply(res);
                    return res;
                })
                .otherwise(error -> {
                    message.fail(-500, "server error");
                    return null;
                });

    }
}
