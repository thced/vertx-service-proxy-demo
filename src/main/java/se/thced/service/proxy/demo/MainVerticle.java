package se.thced.service.proxy.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.thced.service.proxy.demo.greeting.CreateGreetingVerticle;
import se.thced.service.proxy.demo.hello.ReceiveHelloRequestVerticle;

public class MainVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void start(Promise<Void> startPromise) {
    deployVerticles()
        .onSuccess(v -> log.info("{} started..", getClass().getSimpleName()))
        .onComplete(startPromise);
  }

  /** Deploy the top verticles for this project */
  private Future<Void> deployVerticles() {
    return deploy(RouterVerticle::new)
        .compose(ignore -> deploy(ReceiveHelloRequestVerticle::new))
        .compose(ignore -> deploy(CreateGreetingVerticle::new))
        .mapEmpty();
  }

  private Future<Void> deploy(Supplier<Verticle> supplier) {
    return vertx.deployVerticle(supplier, new DeploymentOptions()).mapEmpty();
  }
}
