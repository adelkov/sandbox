package com.hc.accounts.core;

import com.hc.accounts.core.data.UserDTOMemory;
import com.hc.accounts.core.data.UserModel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class AccountsService extends AbstractVerticle {

    private UserDTOMemory userDTOMemory;

    public AccountsService(UserDTOMemory userDTOMemory) {
        this.userDTOMemory = userDTOMemory;
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer("ADD_USER", this::addUser);
        vertx.eventBus().consumer("DEPOSIT", this::deposit);
        vertx.eventBus().consumer("WITHDRAW", this::withdraw);
    }

    private void addUser(final Message<JsonObject> message) {
        UserModel user = new UserModel(
                message.body().getString("userName"),
                message.body().getString("userId"),
                message.body().getInteger("balance")
        );
        userDTOMemory.addUser(user);

        message.reply(JsonObject.mapFrom(user));
    }

    private void deposit(Message message) {

    }

    private void withdraw(Message message) {

    }
}
