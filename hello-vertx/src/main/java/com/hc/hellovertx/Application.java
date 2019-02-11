package com.hc.hellovertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import static io.vertx.ext.jdbc.JDBCClient.createShared;

public class Application extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();

        JsonObject config = new JsonObject()
                .put("url", "jdbc:postgresql://localhost:5432/accounts")
                .put("driver_class", "org.postgresql.Driver")
                .put("user", "kovacsadel");
        SQLClient client = JDBCClient.createShared(vertx, config);

        client.getConnection(res -> {
            if (res.succeeded()) {
                SQLConnection connection = res.result();

//                connection.query(
//                        "DROP TABLE users; CREATE TABLE users (userid varchar(30), username varchar(255), balance int );",
//                        result3 -> {
//                            if (result3.succeeded()) {
//                                System.out.println("table created...");
//                            } else {
//                                System.out.println(result3.cause().getMessage());
//                            }
//                        });

                connection.query("INSERT INTO public.users (userid, username, balance)\n" +
                        "VALUES ('valami', 'value2', 1);", result -> {
                    if (result.succeeded()) {
                        System.out.println("okay");
                    } else {
                        System.out.println(result.cause().getMessage());
                    }
                });

                connection.query("SELECT userid, username, balance\n" +
                        "FROM public.users;", res2 -> {
                    if (res2.succeeded()) {

                        ResultSet rs = res2.result();
                        System.out.println(rs.getRows().get(0));
                        // Do something with results
                    } else {
                        System.out.println(res2.cause().getMessage());
                    }
                });
            } else {
                // Failed to get connection - deal with it
            }
        });
    }
}
