package com.hc.hellovertx;

import com.hc.addition.api.AdditionEventbusClient;
import com.hc.addition.core.AdditionService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;


public class Application extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();

        vertx.deployVerticle(new AdditionService());
        AdditionEventbusClient additionEventbusClient = new AdditionEventbusClient(vertx.eventBus());

        JsonObject object = new JsonObject().put("x", "12").put("y", "111");
        additionEventbusClient.add(object).setHandler(res -> {
            System.out.println(res.result());
        });
        System.out.println("Hello Vert.x!");
    }
}
