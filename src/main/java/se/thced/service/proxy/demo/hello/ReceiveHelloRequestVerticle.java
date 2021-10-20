package se.thced.service.proxy.demo.hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveHelloRequestVerticle extends AbstractVerticle {

  // public static final String ADDRESS = "hello.request";

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void start() {
    vertx.eventBus().consumer("hello.request", this::handleHelloRequest);
    log.info("{} started..", getClass().getSimpleName());
  }

  private void handleHelloRequest(Message<JsonObject> message) {
    var body = message.body();
    delegateCreationOfGreeting(body)
      .onSuccess(message::reply)
      .onFailure(throwable -> message.fail(500, throwable.getMessage()));
  }

  private Future<JsonObject> delegateCreationOfGreeting(JsonObject body) {
    return vertx
        .eventBus()
        .<JsonObject>request("generate.greeting.for.hello", body)
        .map(Message::body);
  }
}
