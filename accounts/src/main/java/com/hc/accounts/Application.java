package com.hc.accounts;

import com.hc.accounts.core.AccountsService;
import com.hc.accounts.core.data.UserDTOMemory;
import com.hc.accounts.core.data.UserModel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;

public class Application extends AbstractVerticle {
    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        super.start();
        vertx.deployVerticle(new AccountsService(new UserDTOMemory()));
        this.eventBus = vertx.eventBus();
        addUserTest();
    }

    private void addUserTest() {
        JsonObject user = new JsonObject()
                .put("userName", "Alma Fa")
                .put("userId", "Id131313")
                .put("balance", 0);
        this.eventBus.send("ADD_USER", user, res -> {
            res.map(result -> {
                System.out.println(result.body());
                return result;
            }).otherwise(error -> {
                System.out.println(error.getMessage());
                return null;
            });
        });
    }
}
