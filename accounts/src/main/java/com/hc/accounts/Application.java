package com.hc.accounts;

import com.hc.accounts.api.AccountsEventBusClient;
import com.hc.accounts.core.AccountsService;
import com.hc.accounts.core.RestAPI;
import com.hc.accounts.core.data.UserDAOMemory;
import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;


public class Application extends AbstractVerticle {
    private EventBus eventBus;
    private AccountsEventBusClient accountsEventbusClient;

    @Override
    public void start() throws Exception {
        super.start();

        vertx.deployVerticle(new RestAPI());
        vertx.deployVerticle(new AccountsService(new UserDAOMemory()));

        this.eventBus = vertx.eventBus();
        this.accountsEventbusClient = new AccountsEventBusClient(this.eventBus);

        addUserTest();
        withdrawTest();
        depositTest();
    }

    private void withdrawTest() {
        WithdrawRequest request = new WithdrawRequest("AK2018/1", 21);
        this.accountsEventbusClient.withDraw(request).map(result -> {
            System.out.println("Withdraw test result (new balance): " + result);
            return result;
        }).otherwise(error -> {
            System.out.println(error.getMessage());
            return null;
        });
    }

    private void depositTest() {
        DepositRequest depositRequest = new DepositRequest("AK2018/1", 12323);
        accountsEventbusClient.deposit(depositRequest)
                .map(result -> {
                    System.out.println("Deposit test result (new balance): " + result);
                    return result;
                })
                .otherwise(error -> {
                    System.out.println(error.getMessage());
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
