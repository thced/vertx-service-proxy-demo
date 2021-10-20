package se.thced.service.proxy.demo.greeting;

import static io.vertx.core.Future.succeededFuture;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a greeting to the hello request
 *
 * @author thced
 */
public class CreateGreetingVerticle extends AbstractVerticle {

  // public static final String ADDRESS = "generate.greeting.for.hello";

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void start(Promise<Void> startPromise) {
    vertx
        .eventBus()
        .consumer("generate.greeting.for.hello", this::handleRequestForGreeting)
        .completionHandler(startPromise);
    log.info("{} started..", getClass().getSimpleName());
  }

  /**
   * Receives the eventbus message
   *
   * @param message The message
   */
  private void handleRequestForGreeting(Message<JsonObject> message) {
    var body = message.body();
    generateGreeting(body)
        .onSuccess(message::reply)
        .onFailure(throwable -> message.fail(500, throwable.getMessage()));
  }

  /**
   * Generate a greeting based on hello request contents
   *
   * @param helloJson The hello request contents
   * @return Future containing the greeting json, or a failure
   */
  private Future<JsonObject> generateGreeting(JsonObject helloJson) {
    if (helloJson.getBoolean("hello", false)) { // NOSONAR
      return friendlyGreeting(helloJson);
    } else {
      return strictGreeting(helloJson);
    }
  }

  /**
   * Create a 'friendly' greeting
   *
   * @param helloJson The hello request contents
   * @return Future containing the friendly greeting, or a failure
   */
  private Future<JsonObject> friendlyGreeting(JsonObject helloJson) {
    var friendly = new JsonObject();
    friendly.put("name", helloJson.getString("name"));
    friendly.put("phrase", "How nice to see you!");

    return succeededFuture(friendly);
  }

  /**
   * Create a 'strict' greeting
   *
   * @param helloJson The hello request contents
   * @return Future containing the strict greeting, or a failure
   */
  private Future<JsonObject> strictGreeting(JsonObject helloJson) {
    var strict = new JsonObject();
    strict.put("name", helloJson.getString("name"));
    strict.put("phrase", "Hi");

    return succeededFuture(strict);
  }
}
