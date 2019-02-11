package com.hc.webshop;

import com.hc.accounts.api.AccountsEventBusClient;
import com.hc.accounts.core.AccountsService;
import com.hc.accounts.core.data.UserDAODatabase;
import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

import static io.vertx.ext.jdbc.JDBCClient.createShared;

public class Application extends AbstractVerticle {
    private EventBus eventBus;
    private AccountsEventBusClient accountsEventbusClient;

    @Override
    public void start() throws Exception {
        super.start();

        deployAccountsService();

        this.eventBus = vertx.eventBus();
        this.accountsEventbusClient = new AccountsEventBusClient(this.eventBus);
    }

    private void deployAccountsService() {
        JsonObject config = new JsonObject()
                .put("url", "jdbc:postgresql://localhost:5432/accounts")
                .put("driver_class", "org.postgresql.Driver")
                .put("user", "kovacsadel");
        SQLClient client = createShared(vertx, config);
        vertx.deployVerticle(new AccountsService(new UserDAODatabase(client)), r -> {
            depositTest();
            withdrawTest();
            addUserTest();
        });
    }

    private void withdrawTest() {
        WithdrawRequest request = new WithdrawRequest("Id131313", 10);
        this.accountsEventbusClient.withDraw(request).map(result -> {
            System.out.println("Withdraw test result (new balance): " + result);
            return result;
        }).otherwise(error -> {
            System.out.println("Application error message: " + error.getMessage());
            return null;
        });
    }

    private void depositTest() {
        DepositRequest depositRequest = new DepositRequest("Id131313", 1000);
        accountsEventbusClient.deposit(depositRequest)
                .map(result -> {
                    System.out.println("Deposit test result (new balance): " + result);
                    return result;
                })
                .otherwise(error -> {
                    System.out.println("Application error message: " + error.getMessage());
                    return null;
                });
    }

    private void addUserTest() {
        UserModel user = new UserModel("Alma Fa", "Id131313", 0);
        this.accountsEventbusClient.addUser(user)
                .map(result -> {
                    System.out.println("Add user test result: " + result);
                    return result;
                })
                .otherwise(error -> {
                    System.out.println(error.getMessage());
                    return null;
                });
    }
}
