package com.hc.accounts;

import com.hc.accounts.core.AccountsService;
import com.hc.accounts.core.data.UserDAODatabase;
import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(VertxUnitRunner.class)
public class AccountServiceTest {

    private Vertx vertx;
    private EventBus eventBus;
    private UserDAODatabase userDAODatabase;
    private String verticleDeployId;

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    @Before
    public void before(TestContext context) {
        vertx = rule.vertx();
        eventBus = vertx.eventBus();
        Async async = context.async();

        mockUserDAODatabase();

        vertx.deployVerticle(new AccountsService(userDAODatabase), id -> {
            verticleDeployId = id.result();
            async.complete();
        });
    }

    private void mockUserDAODatabase() {
        userDAODatabase = Mockito.mock(UserDAODatabase.class);
        when(userDAODatabase.deposit(any(DepositRequest.class))).thenReturn(Future.succeededFuture(12));
        when(userDAODatabase.withdraw(any(WithdrawRequest.class))).thenReturn(Future.succeededFuture(20));
        when(userDAODatabase.addUser(any(UserModel.class))).thenReturn(Future.succeededFuture(new UserModel("lma", "myIDisThis", 12)));
    }

    @After
    public void after(TestContext context) {
        Async async = context.async();
        vertx.undeploy(verticleDeployId, r -> {
            async.complete();
        });
    }


    @Test
    public void depositHappyScenario(TestContext context) {
        Async async = context.async();
        DepositRequest request = new DepositRequest("valami", 3232);
        eventBus.send("DEPOSIT", JsonObject.mapFrom(request), res -> {
            verify(userDAODatabase).deposit(any(DepositRequest.class));
            assertThat(res.result().body()).isEqualTo(12);
            async.complete();
        });
    }

    @Test
    public void depositNegativeAmount(TestContext context) {
        Async async = context.async();
        DepositRequest request = new DepositRequest("valami", -3232);
        eventBus.send("DEPOSIT", JsonObject.mapFrom(request), res -> {
            if (res.succeeded()) {
                context.fail(res.cause().getMessage());
                async.complete();
            } else {
                assertThat(res.cause().getMessage()).isEqualTo("negative amount");
                async.complete();
            }
        });
    }

    @Test
    public void withdrawHappyScenario(TestContext context) {
        Async async = context.async();
        WithdrawRequest request = new WithdrawRequest("valami", 3232);
        eventBus.send("WITHDRAW", JsonObject.mapFrom(request), res -> {
            verify(userDAODatabase).withdraw(any(WithdrawRequest.class));
            assertThat(res.result().body()).isEqualTo(20);
            async.complete();
        });
    }

    @Test
    public void withdrawNegativeAmount(TestContext context) {
        Async async = context.async();
        WithdrawRequest request = new WithdrawRequest("valami", -3232);
        eventBus.send("WITHDRAW", JsonObject.mapFrom(request), res -> {
            if (res.succeeded()) {
                context.fail(res.cause().getMessage());
                async.complete();
            } else {
                assertThat(res.cause().getMessage()).isEqualTo("negative amount");
                async.complete();
            }
        });
    }


    @Test
    public void addUserHappyScenario(TestContext context) {
        Async async = context.async();
        UserModel userModel = new UserModel("almafa", "myIDisThis", 2);
        eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res -> {
            if (res.succeeded()) {
                ArgumentCaptor<UserModel> userArgument = ArgumentCaptor.forClass(UserModel.class);
                verify(userDAODatabase).addUser(userArgument.capture());
                assertThat(userArgument.getValue().getUserId()).isEqualTo("myIDisThis");
                assertThat(res.result().body()).isEqualTo("myIDisThis");
                async.complete();
            } else {
                context.fail(res.cause().getMessage());
                async.complete();
            }
        });
    }

    @Test
    public void addUserMissingUserName(TestContext context) {
        Async async = context.async();
        JsonObject userModel = new JsonObject().put("userId", "dsfsdf").put("balance", 31232);
        eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res -> {
            if (res.failed()) {
                assertThat(res.cause().getMessage()).isEqualTo("missing userName");
                async.complete();
            } else {
                context.fail(res.cause().getMessage());
                async.complete();
            }
        });
    }

    @Test
    public void addUserMissingUserId(TestContext context) {
        Async async = context.async();
        JsonObject userModel = new JsonObject().put("userName", "dsfsdf").put("balance", 31232);
        eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res -> {
            if (res.failed()) {
                assertThat(res.cause().getMessage()).isEqualTo("missing userId");
                async.complete();
            } else {
                context.fail(res.cause().getMessage());
                async.complete();
            }
        });
    }
}