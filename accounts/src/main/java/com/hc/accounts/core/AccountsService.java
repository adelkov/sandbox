package com.hc.accounts.core;

import com.hc.accounts.core.data.UserDAOMemory;
import com.hc.accounts.core.data.models.UserModel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class AccountsService extends AbstractVerticle {

    private UserDAOMemory userDAOMemory;

    public AccountsService(UserDAOMemory userDAOMemory) {
        this.userDAOMemory = userDAOMemory;
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

        if (userDAOMemory.getUserWithUserId(user.getUserId()) == null) {
            userDAOMemory.addUser(user);
        }
        message.reply(user.getUserId());
    }

    private void deposit(final Message<JsonObject> message) {
        if (message.body().getString("userId") == null) {
            message.fail(-404, "missing userId");
            return;
        }

        UserModel user = userDAOMemory.getUserWithUserId(message.body().getString("userId"));

        if (message.body().getInteger("amount") == null) {
            message.fail(-404, "missing amount");
            return;
        }

        if (message.body().getInteger("amount") < 0) {
            message.fail(-404, "negative amount");
            return;
        }

        user.deposit(message.body().getInteger("amount"));
        message.reply(user.getBalance());
    }

    private void withdraw(final Message<JsonObject> message) {
        if (message.body().getString("userId") == null) {
            message.fail(-404, "missing userId");
            return;
        }

        UserModel user = userDAOMemory.getUserWithUserId(message.body().getString("userId"));

        if (message.body().getInteger("amount") == null) {
            message.fail(-404, "missing amount");
            return;
        }

        if (message.body().getInteger("amount") < 0) {
            message.fail(-404, "negative amount");
            return;
        }

        user.withdraw(message.body().getInteger("amount"));
        message.reply(user.getBalance());
    }
}
