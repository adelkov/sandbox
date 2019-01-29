package com.hc.hellovertx;

import com.hc.addition.api.AdditionEventbusClient;
import com.hc.addition.api.AdditionRestClient;
import com.hc.addition.core.AdditionService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import java.util.function.Function;


public class Application extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();
        JsonObject addends = new JsonObject().put("x", "12").put("y", "111");

        // Event bus client
        vertx.deployVerticle(new AdditionService());
        AdditionEventbusClient additionEventbusClient = new AdditionEventbusClient(vertx.eventBus());
        additionEventbusClient.add(addends).map(res -> {
            System.out.println("Result with EventBus client: " + res);
            return res;
        });

        // Rest client
        WebClientOptions options = new WebClientOptions();
        options
                .setDefaultHost("localhost")
                .setDefaultPort(8080)
                .addCrlPath("/add");
        WebClient webClient = WebClient.create(vertx, options);
        AdditionRestClient additionRestClient = new AdditionRestClient(webClient);
        additionRestClient.add(addends)
                .map(res -> {
                    System.out.println("Result with rest client: " + res);
                    return res;
                }).otherwise(error -> {
            System.out.println(error.getMessage());
            return null;
        });
    }
}
