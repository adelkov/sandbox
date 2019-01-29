package com.hc.accounts;

import io.vertx.core.AbstractVerticle;

public class Application extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("accounts service starts...");
    }
}
