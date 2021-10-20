package se.thced.service.proxy.demo.handlers;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** First request handler that receives the web request */
public class HelloHandler implements Handler<RoutingContext> {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final Vertx vertx;

  public HelloHandler(Vertx vertx) {
    this.vertx = vertx;
  }

  /**
   * We received a 'Hello' request, time to create a proper greeting
   *
   * <p>The "API" for Hello is:
   *
   * <pre>
   * {
   *   "hello": &lt;bool&gt;,
   *   "name": &lt;string&gt;
   * }</pre>
   *
   * with an expected response:
   *
   * <pre>
   * {
   *   "name": &lt;string&gt;,
   *   "phrase": &lt;string&gt;
   * }</pre>
   */
  @Override
  public void handle(RoutingContext ctx) {
    var body = ctx.getBodyAsJson();
    vertx
        .eventBus()
        .<JsonObject>request("hello.request", body)
        .map(Message::body)
        .onSuccess(json -> log.info("Replying: {}", json.encode()))
        .onSuccess(json -> ctx.json(json))
        .onFailure(throwable -> ctx.fail(throwable));
  }
}
