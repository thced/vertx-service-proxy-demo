package se.thced.service.proxy.demo.greeting;

import static org.junit.jupiter.api.Assertions.*;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.*;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(VertxExtension.class)
class CreateGreetingVerticleTest {

  @BeforeEach
  void setUp(Vertx vertx, VertxTestContext testContext) {
    vertx
        .deployVerticle(new CreateGreetingVerticle())
        .onComplete(testContext.succeedingThenComplete());
  }

  @Test
  @Timeout(2000)
  void thatFriendlyHelloGenerateFriendlyGreeting(Vertx vertx, VertxTestContext testContext) {
    var jsonToSend = pigletJson().put("hello", true);

    request(vertx, jsonToSend)
        .map(Message::body)
        .onSuccess(
            json ->
                testContext.verify(
                    () -> {
                      assertEquals("Piglet", json.getString("name"));
                      assertTrue(json.getString("phrase").contains("see you!"));
                    }))
        .onComplete(testContext.succeedingThenComplete());
  }

  @Test
  @Timeout(2000)
  void thatStrictHelloGenerateStrictGreeting(Vertx vertx, VertxTestContext testContext) {
    var jsonToSend = pigletJson().put("hello", false);

    request(vertx, jsonToSend)
      .map(Message::body)
      .onSuccess(
        json ->
          testContext.verify(
            () -> {
              assertEquals("Piglet", json.getString("name"));
              assertTrue(json.getString("phrase").contains("Hi"));
            }))
      .onComplete(testContext.succeedingThenComplete());
  }

  @Test
  @Timeout(2000)
  void thatAbsentHelloGenerateStrictGreeting(Vertx vertx, VertxTestContext testContext) {
    var jsonToSend = pigletJson();

    request(vertx, jsonToSend)
      .map(Message::body)
      .onSuccess(
        json ->
          testContext.verify(
            () -> {
              assertEquals("Piglet", json.getString("name"));
              assertTrue(json.getString("phrase").contains("Hi"));
            }))
      .onComplete(testContext.succeedingThenComplete());
  }

  private JsonObject pigletJson() {
    return new JsonObject().put("name", "Piglet");
  }

  private <T> Future<Message<T>> request(Vertx vertx, T entity) {
    return vertx.eventBus().request("generate.greeting.for.hello", entity);
  }
}
