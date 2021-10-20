package se.thced.service.proxy.demo.hello;

import static org.junit.jupiter.api.Assertions.*;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(VertxExtension.class)
class ReceiveHelloRequestVerticleTest {

  @BeforeEach
  void setUp(Vertx vertx, VertxTestContext testContext) {
    var whatToReplyWith = new JsonObject("""
      {
        "name": "Jack",
        "phrase": "How nice to see you!"
      }
      """);
    vertx.eventBus().consumer("generate.greeting.for.hello", message -> message.reply(whatToReplyWith));

    vertx
        .deployVerticle(new ReceiveHelloRequestVerticle())
        .onComplete(testContext.succeedingThenComplete());
  }

  @Test
  void thatReceivesGreetingFromHello(Vertx vertx, VertxTestContext testContext) {
    vertx
      .eventBus()
      .<JsonObject>request("hello.request", new JsonObject())
      .map(Message::body)
      .onSuccess(
        json ->
          testContext.verify(
            () -> {
              assertEquals("Jack", json.getString("name"));
              assertNotNull(json.getString("phrase"));
            }))
      .onComplete(testContext.succeedingThenComplete());
  }
}
