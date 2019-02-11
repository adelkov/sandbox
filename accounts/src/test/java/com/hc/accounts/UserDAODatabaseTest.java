package com.hc.accounts;

import com.hc.accounts.core.data.UserDAODatabase;
import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(VertxUnitRunner.class)
public class UserDAODatabaseTest {


    SQLClient client;
    ResultSet resultSet;
    AsyncResult<ResultSet> resp;
    private UserDAODatabase userDAODatabase;

    @Before
    public void before(TestContext context) {
        mockClient();
        userDAODatabase = new UserDAODatabase(client);
    }

    private void mockClient() {

        // mock objects
        client = Mockito.mock(SQLClient.class);
        resultSet = Mockito.mock(ResultSet.class);
        resp = (AsyncResult<ResultSet>) Mockito.mock(AsyncResult.class);

        // sample resultset
        JsonObject user = new JsonObject().put("userid", "valami1").put("username", "almafa kutya").put("balance", 10);
        JsonObject user2 = new JsonObject().put("userid", "valami2").put("username", "almafa cica").put("balance", 10);
        List<JsonObject> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        // mock behaviour
        when(resultSet.getRows()).thenReturn(users);
        when(resp.succeeded()).thenReturn(true);
        when(resp.result()).thenReturn(resultSet);
        ArgumentCaptor<Handler<AsyncResult<ResultSet>>> argument = ArgumentCaptor.forClass(Handler.class);

        when(client.query(any(String.class), argument.capture())).then(res -> {
            argument.getValue().handle(resp);
            return null;
        });
    }

    @Test
    public void getUserReturnsInitialUser(TestContext context) {
        Async async = context.async();
        userDAODatabase.getUsers()
                .map(res -> {
                    verify(client).query("SELECT userid, username, balance\n" +
                            "FROM public.users;", any(Handler.class));
                    assertThat(res.size()).isEqualTo(2);
                    async.complete();
                    return res;
                })
                .otherwise(error -> {
                    context.fail(error.getMessage());
                    return null;
                });
    }

    @Test
    public void getUserWithId(TestContext context) {
        Async async = context.async();
        userDAODatabase.getUserWithUserId("valami1")
                .map(res -> {
                    verify(client).query("SELECT userid, username, balance\n" +
                            "FROM public.users\n" +
                            "WHERE userid='valami1';", any(Handler.class));
                    assertThat(res.getUserId()).isEqualTo("valami1");
                    async.complete();
                    return res;
                })
                .otherwise(error -> {
                    context.fail(error.getMessage());
                    async.complete();
                    return null;
                });
    }

    @Test
    public void addUser(TestContext context) {
        Async async = context.async();
        UserModel user = new UserModel("valami3", "almafa", 3);
        userDAODatabase.addUser(user)
                .map(res -> {
                    verify(client).query("INSERT INTO public.users (userid, username, balance) VALUES ('almafa', 'valami3', '3');",
                            any(Handler.class));
                    assertThat(res.getUserId()).isEqualTo("almafa");
                    async.complete();
                    return res;
                })
                .otherwise(error -> {
                    context.fail(error.getMessage());
                    async.complete();
                    return null;
                });
    }

    @Test
    public void withdraw(TestContext context) {
        Async async = context.async();
        WithdrawRequest request = new WithdrawRequest("valami2", 10);
        userDAODatabase.withdraw(request)
                .map(res -> {
                    verify(client).query("UPDATE public.users\n" +
                            "SET balance = '10'\n" +
                            "WHERE userid='valami1';", any(Handler.class));
                    assertThat(res).isEqualTo(0);
                    async.complete();
                    return res;
                })
                .otherwise(error -> {
                    context.fail(error.getMessage());
                    async.complete();
                    return null;
                });
    }

    @Test
    public void deposit(TestContext context) {
        Async async = context.async();
        DepositRequest request = new DepositRequest("valami2", 10);
        userDAODatabase.deposit(request)
                .map(res -> {
                    System.out.println("returning from dao");
                    verify(client).query("UPDATE public.users\n" +
                            "SET balance = '10'\n" +
                            "WHERE userid='valami1';", any(Handler.class));
                    assertThat(res).isEqualTo(20);
                    async.complete();
                    return res;
                })
                .otherwise(error -> {
                    context.fail(error.getMessage());
                    async.complete();
                    return null;
                });
    }
}
