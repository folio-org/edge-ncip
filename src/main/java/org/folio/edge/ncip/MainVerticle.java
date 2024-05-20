package org.folio.edge.ncip;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.edge.core.EdgeVerticleHttp;
import org.folio.edge.core.utils.OkapiClientFactory;
import org.folio.edge.ncip.utils.NcipOkapiClientFactory;

public class MainVerticle extends EdgeVerticleHttp {
  private static final Logger logger = LogManager.getLogger(MainVerticle.class);

  @Override
  public Router defineRoutes() {
    logger.info("BouncyCastleFipsProvider has been added");
    OkapiClientFactory ocf = NcipOkapiClientFactory.createInstance(vertx, config());
    NcipHandler ncipHandler = new NcipHandler(secureStore, ocf);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route(HttpMethod.POST, "/ncip/:apiKeyPath").handler(ncipHandler::handle);
    router.route(HttpMethod.POST, "/ncip").handler(ncipHandler::handle);
    router.route(HttpMethod.GET, "/ncipconfigcheck").handler(ncipHandler::handleConfigCheck);
    router.route(HttpMethod.GET, "/nciphealthcheck").handler(ncipHandler::handleHealthCheck);
    router.route(HttpMethod.GET, "/admin/health").handler(this::handleHealthCheck);
    return router;
  }

}
