package com.hc.accounts;

import com.hc.accounts.core.AccountsService;
import com.hc.accounts.core.data.UserDTOMemory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;


public class Application extends AbstractVerticle {
    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        super.start();
        vertx.deployVerticle(new AccountsService(new UserDTOMemory()));
        this.eventBus = vertx.eventBus();
        addUserTest();
        withrawTest();
        depositTest();
    }

    private void withrawTest() {
        JsonObject request = new JsonObject()
                .put("userName", "Alma Fa")
                .put("amount", 10);
        this.eventBus.send("WITHDRAW", request, res -> {
            res.map(result -> {
                System.out.println("Withdraw test result (new balance): " + result.body());
                return result;
            }).otherwise(error -> {
                System.out.println(error.getMessage());
                return null;
            });
        });
    }


    private void depositTest() {
        JsonObject request = new JsonObject()
                .put("userName", "Alma Fa")
                .put("amount", 100);
        this.eventBus.send("DEPOSIT", request, res -> {
            res.map(result -> {
                System.out.println("Deposit test result (new balance): " + result.body());
                return result;
            }).otherwise(error -> {
                System.out.println(error.getMessage());
                return null;
            });
        });
    }

    private void addUserTest() {
        JsonObject user = new JsonObject()
                .put("userName", "Alma Fa")
                .put("userId", "Id131313")
                .put("balance", 0);
        this.eventBus.send("ADD_USER", user, res -> {
            res.map(result -> {
                System.out.println("Add user test result: " + result.body());
                return result;
            }).otherwise(error -> {
                System.out.println(error.getMessage());
                return null;
            });
        });
    }
}
