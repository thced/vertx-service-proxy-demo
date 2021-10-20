package se.thced.service.proxy.demo;

import static io.reactiverse.junit5.web.TestRequest.jsonBodyResponse;
import static io.reactiverse.junit5.web.TestRequest.requestHeader;
import static io.reactiverse.junit5.web.TestRequest.statusCode;
import static io.reactiverse.junit5.web.TestRequest.statusMessage;
import static io.reactiverse.junit5.web.TestRequest.testRequest;

import io.reactiverse.junit5.web.TestRequest;
import io.reactiverse.junit5.web.WebClientOptionsInject;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.*;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(VertxExtension.class)
class RouterVerticleTest implements WebClientSupport {

//  @WebClientOptionsInject
//  public WebClientOptions webClientOptions =
//      new WebClientOptions().setDefaultHost("localhost").setDefaultPort(8080);

  @BeforeEach
  void setUp(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new RouterVerticle()).onComplete(testContext.succeedingThenComplete());

    vertx.eventBus().consumer("hello.request", message -> message.reply(new JsonObject("""
      {
        "name" : "Thomas",
        "phrase" : "hi"
      }
      """)));
  }

  @Test
  @Timeout(5000)
  void thatRouterAcceptRequests(VertxTestContext testContext, WebClient client) {
    testRequest(client, HttpMethod.POST, "/")
      .with(requestHeader("Content-Type", "application/json"))
      .expect(statusCode(200), statusMessage("OK"))
      .expect(jsonBodyResponse(new JsonObject("""
        {
          "name" : "Thomas",
          "phrase" : "hi"
        }
        """)))
      .send(testContext);
  }
}
