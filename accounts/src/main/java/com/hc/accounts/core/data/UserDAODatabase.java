package com.hc.accounts.core.data;

import com.hc.accounts.core.data.models.DepositRequest;
import com.hc.accounts.core.data.models.UserModel;
import com.hc.accounts.core.data.models.WithdrawRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;

import java.util.HashMap;
import java.util.Map;

public class UserDAODatabase implements UserDAOInterface {

    private SQLClient client;

    public UserDAODatabase(SQLClient client) {
        this.client = client;
    }

    @Override
    public AsyncResult<UserModel> getUserWithUserId(String userId) {
        return Future.future(fut -> {
            String getUserWithIdSql = String.format("SELECT userid, username, balance\n" +
                    "FROM public.users\n" +
                    "WHERE userid='%s';", userId);
            client.query(getUserWithIdSql, res -> {
                if (res.succeeded()) {
                    ResultSet resultSet = res.result();
                    JsonObject userObj = resultSet.getRows().get(0);
                    UserModel userModel = parseUserFromJSONObject(userObj);
                    fut.complete(userModel);
                } else {
                    fut.fail(res.cause());
                }
            });
        });
    }

    private UserModel parseUserFromJSONObject(JsonObject userObj) {
        UserModel userModel = new UserModel();
        userModel.setUserName(userObj.getString("username"));
        userModel.setUserId(userObj.getString("userid"));
        userModel.setBalance(userObj.getValue("balance").toString());
        return userModel;
    }


    @Override
    public AsyncResult<UserModel> addUser(UserModel user) {
        return Future.future(fut -> {
            String query = String.format("INSERT INTO public.users (userid, username, balance) VALUES ('%s', '%s', '%d');",
                    user.getUserId(), user.getUserName(), user.getBalance());
            client.query(query, res -> {
                if (res.succeeded()) {
                    fut.complete(user);
                } else {
                    fut.fail(res.cause());
                }
            });
        });
    }

    @Override
    public AsyncResult<Map<String, UserModel>> getUsers() {
        return Future.future(fut -> {
            String getUserWithIdSql = String.format("SELECT userid, username, balance\n" +
                    "FROM public.users;");
            client.query(getUserWithIdSql, res -> {
                if (res.succeeded()) {
                    ResultSet resultSet = res.result();
                    Map<String, UserModel> usersMap = new HashMap<>();
                    resultSet.getRows().forEach(user -> {
                        usersMap.put(user.getString("userid"), parseUserFromJSONObject(user));
                    });
                    fut.complete(usersMap);
                } else {
                    fut.fail(res.cause().getMessage());
                }
            });
        });
    }

    @Override
    public AsyncResult<Integer> withdraw(WithdrawRequest request) {

        String query = String.format("UPDATE public.users\n" +
                "SET balance = '%d'\n" +
                "WHERE userid='%s';", request.amount, request.userId);
        return Future.future(fut -> {
            this.getUserWithUserId(request.userId)
                    .map(res -> {
                        int newBalance = res.withdraw(request.amount);
                        client.query(query, res2 -> {
                            if (res2.succeeded()) {
                                fut.complete(newBalance);
                            } else {
                                fut.fail(res2.cause());
                            }
                        });
                        return res;
                    });
        });
    }

    @Override
    public AsyncResult<Integer> deposit(DepositRequest request) {
        String query = String.format("UPDATE public.users\n" +
                "SET balance = '%d'\n" +
                "WHERE userid='%s';", request.amount, request.userId);
        return Future.future(fut -> {
            this.getUserWithUserId(request.userId)
                    .map(res -> {
                        int newBalance = res.deposit(request.amount);
                        client.query(query, res2 -> {
                            if (res2.succeeded()) {
                                fut.complete(newBalance);
                            } else {
                                fut.fail(res2.cause());
                            }
                        });
                        return res;
                    });
        });
    }

}
