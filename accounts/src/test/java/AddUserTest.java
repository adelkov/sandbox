import com.hc.accounts.core.AccountsService;
import com.hc.accounts.core.data.UserDAOMemory;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(VertxUnitRunner.class)
public class AddUserTest {

    private Vertx vertx;
    private EventBus eventBus;
    private UserDAOMemory userDAOMemory;
    private String verticleDeployId;

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    @Before
    public void before(TestContext context) {
        vertx = rule.vertx();
        eventBus = vertx.eventBus();
        userDAOMemory = new UserDAOMemory();
        Async async = context.async();
        vertx.deployVerticle(new AccountsService(userDAOMemory), id -> {
            verticleDeployId = id.result();
            async.complete();
        });
    }

    @After
    public void after(TestContext context) {
        Async async = context.async();
        vertx.undeploy(verticleDeployId, r -> {
            async.complete();
        });
    }

    @Test
    public void addUserReturnsUserId(TestContext context) {
        Async async = context.async();
        UserModel userModel = new UserModel("Almafa", "id3", 2);
        eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res -> {
            if (res.succeeded()) {
                context.verify(r -> {
                    assertThat(res.result().body()).isEqualTo("id3");
                    async.complete();
                });
            } else {
                context.fail(res.cause());
            }
        });
    }

    @Test
    public void addUserSavesToMemory(TestContext context) {
        Async async = context.async();
        UserModel userModel = new UserModel("Kutyafa", "id32", 200);
        eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res -> {
            if (res.succeeded()) {
                UserModel userModel1 = userDAOMemory.getUserWithUserId("id32");
                context.verify(r -> {
                    assertThat(userModel1).isEqualToComparingFieldByField(userModel);
                    async.complete();
                });
            } else {
                context.fail(res.cause());
            }
        });
    }

    @Test
    public void addUserUserSavedToMemory(TestContext context) {
        Async async = context.async();
        UserModel userModel = new UserModel("Almafa Béla", "id2", 210);
        int usersBeforeAddition = this.userDAOMemory.getUsers().size();
        eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res -> {
            if (res.succeeded()) {
                int usersAfterAddition = this.userDAOMemory.getUsers().size();
                context.verify(r -> {
                    assertThat(usersAfterAddition - usersBeforeAddition).isEqualTo(1);
                });
                async.complete();
            } else {
                context.fail(res.cause());
            }
        });
    }

    @Test
    public void addUserDoesntDuplicate(TestContext context) {
        Async async = context.async();
        UserModel userModel = new UserModel("Almafa Béla", "id2", 210);
        eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res -> {
            if (res.succeeded()) {
                int usersBeforeAddition = this.userDAOMemory.getUsers().size();
                eventBus.send("ADD_USER", JsonObject.mapFrom(userModel), res2 -> {
                    if (res2.succeeded()) {
                        int usersAfterAddition = this.userDAOMemory.getUsers().size();
                        context.verify(r -> {
                            assertThat(usersAfterAddition - usersBeforeAddition).isEqualTo(0);
                        });
                        async.complete();
                    } else {
                        context.fail(res2.cause());
                    }
                });
            } else {
                context.fail(res.cause());
            }
        });
    }

    @Test
    public void addUserMissingUserNameThrowsNullpointer(TestContext context) {
        Async async = context.async();
        JsonObject userModel = new JsonObject().put("userId", "dsfsdf").put("balance", 31232);
        eventBus.send("ADD_USER", userModel, res -> {
            if (res.succeeded()) {
                context.fail(res.cause());
            } else {
                if (res.cause() instanceof ReplyException) {
                    ReplyException re = (ReplyException) res.cause();
                    context.verify(r -> {
                        assertThat(-404).isEqualTo(re.failureCode());
                    });
                } else {
                    context.fail("Cause wasn't ReplyException");
                }
                async.complete();
            }
        });
    }

    @Test
    public void withdrawDecreasesBalance(TestContext context) {
        Async async = context.async();
        WithdrawRequest request = new WithdrawRequest("AK2018/1", 210);

        int balanceBeforeWithdrawal = userDAOMemory.getUserWithUserId(request.userId).getBalance();
        eventBus.send("WITHDRAW", JsonObject.mapFrom(request), res -> {
            if (res.succeeded()) {
                context.verify(r -> {
                    int balanceAfterWithdrawal = userDAOMemory.getUserWithUserId(request.userId).getBalance();
                    assertThat(balanceBeforeWithdrawal - balanceAfterWithdrawal).isEqualTo(210);
                });
                async.complete();
            } else {
                context.fail(res.cause());
            }

        });
    }

    @Test
    public void withdrawMissingUserIdThrowsException(TestContext context) {
        Async async = context.async();
        JsonObject request = new JsonObject().put("amount", 31232);
        eventBus.send("WITHDRAW", request, res -> {
            if (res.succeeded()) {
                context.fail(res.cause());
            } else {
                if (res.cause() instanceof ReplyException) {
                    ReplyException re = (ReplyException) res.cause();
                    context.verify(r -> {
                        assertThat(-404).isEqualTo(re.failureCode());
                    });
                } else {
                    context.fail("Cause wasn't ReplyException");
                }
                async.complete();
            }
        });
    }

    @Test
    public void withdrawMissingAmountThrowsException(TestContext context) {
        Async async = context.async();
        JsonObject request = new JsonObject().put("userId", "AK2018/1");
        eventBus.send("WITHDRAW", request, res -> {
            if (res.succeeded()) {
                context.fail(res.cause());
            } else {
                if (res.cause() instanceof ReplyException) {
                    ReplyException re = (ReplyException) res.cause();
                    context.verify(r -> {
                        assertThat(-404).isEqualTo(re.failureCode());
                    });
                } else {
                    context.fail("Cause wasn't ReplyException");
                }
                async.complete();
            }
        });
    }

    @Test
    public void withdrawNegativeAmountThrowsException(TestContext context) {
        Async async = context.async();
        JsonObject request = new JsonObject().put("amount", -10);
        eventBus.send("WITHDRAW", request, res -> {
            if (res.succeeded()) {
                context.fail(res.cause());
            } else {
                if (res.cause() instanceof ReplyException) {
                    ReplyException re = (ReplyException) res.cause();
                    context.verify(r -> {
                        assertThat(-404).isEqualTo(re.failureCode());
                    });
                } else {
                    context.fail("Cause wasn't ReplyException");
                }
                async.complete();
            }
        });
    }
}
