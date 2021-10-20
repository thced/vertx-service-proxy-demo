package se.thced.service.proxy.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.thced.service.proxy.demo.handlers.HelloHandler;

public class RouterVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void start(Promise<Void> startPromise) {
    var options = new HttpServerOptions().setPort(8080);
    var server = vertx.createHttpServer(options);

    createRouter()
      .map(server::requestHandler)
      .compose(HttpServer::listen)
      .<Void>mapEmpty()
      .onSuccess(v -> log.info("Listening on port {}", server.actualPort()))
      .onComplete(startPromise);
  }

  private Future<Router> createRouter() {
    Router router = Router.router(vertx);

    // This handler make sure we can extract a full body -- buffering the whole request
    router.route().handler(BodyHandler.create());

    router.post("/").handler(new HelloHandler(vertx));

    return Future.succeededFuture(router);
  }
}
