package com.hc.accounts;

import com.hc.accounts.api.AccountsEventBusClient;
import com.hc.accounts.core.AccountsService;
import com.hc.accounts.core.RestAPI;
import com.hc.accounts.core.data.UserDAODatabase;
import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;


public class Application extends AbstractVerticle {
    private EventBus eventBus;
    private AccountsEventBusClient accountsEventbusClient;

    @Override
    public void start() throws Exception {
        super.start();

        vertx.deployVerticle(new RestAPI());


        this.eventBus = vertx.eventBus();
        this.accountsEventbusClient = new AccountsEventBusClient(this.eventBus);
        dbTest();
    }

    private void dbTest() {

        JsonObject config = new JsonObject()
                .put("url", "jdbc:postgresql://localhost:5432/accounts")
                .put("driver_class", "org.postgresql.Driver")
                .put("user", "kovacsadel");
        SQLClient client = JDBCClient.createShared(vertx, config);
        vertx.deployVerticle(new AccountsService(new UserDAODatabase(client)));

    }

    private void testGetUsers(UserDAODatabase userDAOdatabase) {
        userDAOdatabase.getUsers()
                .map(res2 -> {
                    System.out.println("TEST: Get users, new size: " + res2.size());
                    return res2;
                })
                .otherwise(error -> {
                    System.out.println(error.getMessage());
                    return null;
                });
    }

    private void testGetUserWithId(UserDAODatabase userDAOdatabase) {
        userDAOdatabase.getUserWithUserId("valami")
                .map(res2 -> {
                    System.out.println("TEST: Get user with id: " + res2.getUserName());
                    return res2;
                });
    }

    private void testAddUser(UserDAODatabase userDAOdatabase) {
        UserModel user = new UserModel("Alma Fa", "Id131313", 0);
        userDAOdatabase.addUser(user)
                .map(res -> {
                    System.out.println("TEST: User added with id: " + res.getUserId());
                    return res;
                })
                .otherwise(error -> {
                    System.out.println(error.getMessage());
                    return null;
                });
    }

    private void testWithDraw(UserDAODatabase userDAOdatabase) {
        WithdrawRequest request = new WithdrawRequest("valami", 100);
        userDAOdatabase.withdraw(request)
                .map(res -> {
                    System.out.println("TEST: Withdraw. New balance:  " + res.toString());
                    return res;
                })
                .otherwise(error -> {
                    System.out.println(error.getMessage());
                    return null;
                });
    }

    private void testDeposit(UserDAODatabase userDAOdatabase) {
        DepositRequest request = new DepositRequest("valami", 10);
        userDAOdatabase.deposit(request)
                .map(res -> {
                    System.out.println("TEST: Deposit. New balance: " + res.toString());
                    return res;
                })
                .otherwise(error -> {
                    System.out.println(error.getMessage());
                    return null;
                });
    }
}
