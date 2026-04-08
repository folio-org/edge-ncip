package org.folio.edge.ncip;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import java.util.function.Supplier;
import org.junit.Test;

public class MainTest {

    @Test
    public void mainUsesConfiguredVertxSupplierToDeployMainVerticle() {
        Vertx vertx = mock(Vertx.class);
        Supplier<Vertx> originalSupplier = Main.vertxSupplier;

        try {
            Main.vertxSupplier = () -> vertx;
            when(vertx.deployVerticle(any(Verticle.class))).thenReturn(Future.succeededFuture("deployment-id"));

            Main.main(new String[0]);

            verify(vertx).deployVerticle(any(MainVerticle.class));
        } finally {
            Main.vertxSupplier = originalSupplier;
        }
    }
}