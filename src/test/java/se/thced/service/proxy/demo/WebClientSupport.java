package se.thced.service.proxy.demo;

import io.reactiverse.junit5.web.WebClientOptionsInject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

/**
 * Adds support for {@link WebClient} test method parameter
 *
 * @author thced
 */
public interface WebClientSupport {

  @WebClientOptionsInject
  WebClientOptions webClientOptions =
      new WebClientOptions().setDefaultHost("localhost").setDefaultPort(8080);
}
