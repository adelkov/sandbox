//package com.hc.accounts;
//
//import com.hc.accounts.core.AccountsService;
//import com.hc.accounts.core.data.UserDAOMemory;
//import com.hc.accounts.core.data.models.WithdrawRequest;
//import io.vertx.core.Vertx;
//import io.vertx.core.eventbus.EventBus;
//import io.vertx.core.eventbus.ReplyException;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.unit.Async;
//import io.vertx.ext.unit.TestContext;
//import io.vertx.ext.unit.junit.RunTestOnContext;
//import io.vertx.ext.unit.junit.VertxUnitRunner;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(VertxUnitRunner.class)
//public class WithdrawTest {
//
//    private Vertx vertx;
//    private EventBus eventBus;
//    private UserDAOMemory userDAOMemory;
//    private String verticleDeployId;
//
//    @Rule
//    public RunTestOnContext rule = new RunTestOnContext();
//
//    @Before
//    public void before(TestContext context) {
//        vertx = rule.vertx();
//        eventBus = vertx.eventBus();
//        userDAOMemory = new UserDAOMemory();
//        Async async = context.async();
//        vertx.deployVerticle(new AccountsService(userDAOMemory), id -> {
//            verticleDeployId = id.result();
//            async.complete();
//        });
//    }
//
//    @After
//    public void after(TestContext context) {
//        Async async = context.async();
//        vertx.undeploy(verticleDeployId, r -> {
//            async.complete();
//        });
//    }
//
//    @Test
//    public void withdrawDecreasesBalance(TestContext context) {
////        Async async = context.async();
////        WithdrawRequest request = new WithdrawRequest("AK2018/1", 210);
////
////        int balanceBeforeWithdrawal = userDAOMemory.getUserWithUserId(request.userId).getBalance();
////        eventBus.send("WITHDRAW", JsonObject.mapFrom(request), res -> {
////            if (res.succeeded()) {
////                context.verify(r -> {
////                    int balanceAfterWithdrawal = userDAOMemory.getUserWithUserId(request.userId).getBalance();
////                    assertThat(balanceBeforeWithdrawal - balanceAfterWithdrawal).isEqualTo(210);
////                });
////                async.complete();
////            } else {
////                context.fail(res.cause());
////            }
////
////        });
//    }
//
//    @Test
//    public void withdrawMissingUserIdThrowsException(TestContext context) {
////        Async async = context.async();
////        JsonObject request = new JsonObject().put("amount", 31232);
////        eventBus.send("WITHDRAW", request, res -> {
////            if (res.succeeded()) {
////                context.fail(res.cause());
////            } else {
////                if (res.cause() instanceof ReplyException) {
////                    ReplyException re = (ReplyException) res.cause();
////                    context.verify(r -> {
////                        assertThat(-404).isEqualTo(re.failureCode());
////                    });
////                } else {
////                    context.fail("Cause wasn't ReplyException");
////                }
////                async.complete();
////            }
////        });
//    }
//
//    @Test
//    public void withdrawMissingAmountThrowsException(TestContext context) {
//        Async async = context.async();
//        JsonObject request = new JsonObject().put("userId", "AK2018/1");
//        eventBus.send("WITHDRAW", request, res -> {
//            if (res.succeeded()) {
//                context.fail(res.cause());
//            } else {
//                if (res.cause() instanceof ReplyException) {
//                    ReplyException re = (ReplyException) res.cause();
//                    context.verify(r -> {
//                        assertThat(-404).isEqualTo(re.failureCode());
//                    });
//                } else {
//                    context.fail("Cause wasn't ReplyException");
//                }
//                async.complete();
//            }
//        });
//    }
//
//    @Test
//    public void withdrawNegativeAmountThrowsException(TestContext context) {
//        Async async = context.async();
//        JsonObject request = new JsonObject().put("amount", -10);
//        eventBus.send("WITHDRAW", request, res -> {
//            if (res.succeeded()) {
//                context.fail(res.cause());
//            } else {
//                if (res.cause() instanceof ReplyException) {
//                    ReplyException re = (ReplyException) res.cause();
//                    context.verify(r -> {
//                        assertThat(-404).isEqualTo(re.failureCode());
//                    });
//                } else {
//                    context.fail("Cause wasn't ReplyException");
//                }
//                async.complete();
//            }
//        });
//    }
//}
