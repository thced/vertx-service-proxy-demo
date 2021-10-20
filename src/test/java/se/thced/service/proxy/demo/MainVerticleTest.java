package se.thced.service.proxy.demo;

import io.vertx.core.Vertx;
import io.vertx.junit5.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.*;

@ExtendWith(VertxExtension.class)
class MainVerticleTest {

  @Test
  @Timeout(2000)
  void thatMainVerticleStarts(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeedingThenComplete());
  }
}
