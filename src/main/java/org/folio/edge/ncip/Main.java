package org.folio.edge.ncip;

import io.vertx.core.Vertx;
import java.util.function.Supplier;

public class Main {

    static Supplier<Vertx> vertxSupplier = Vertx::vertx;

    public static void main(String[] args) {
        deploy(vertxSupplier.get());
    }

    static void deploy(Vertx vertx) {
        vertx.deployVerticle(new MainVerticle())
                .onFailure(Throwable::printStackTrace);
    }
}
