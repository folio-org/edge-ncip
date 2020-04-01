package org.folio.edge.ncip;

import org.folio.edge.core.EdgeVerticle;
import org.folio.edge.ncip.utils.NcipOkapiClientFactory;
import org.apache.log4j.Logger;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.BodyHandler;


public class MainVerticle extends EdgeVerticle {
	
	  private static final Logger logger = Logger.getLogger(MainVerticle.class);

	  public MainVerticle() {
	    super();
	  }

	  @Override
	  public Router defineRoutes() {
	    NcipOkapiClientFactory ocf = new NcipOkapiClientFactory(vertx, okapiURL, reqTimeoutMs);
	    NcipHandler ncipHandler = new NcipHandler(secureStore, ocf);
	    Router router = Router.router(vertx);
	    router.route().handler(BodyHandler.create());
	    router.route(HttpMethod.POST, "/ncip").handler(ncipHandler::handle);
	    router.route(HttpMethod.GET,"/ncipconfigcheck").handler(ncipHandler::handleConfigCheck);
	    router.route(HttpMethod.GET, "/nciphealthcheck").handler(ncipHandler::handleHealthCheck);
            router.route(HttpMethod.GET, "/admin/health").handler(this::handleHealthCheck);
	    return router;
	  }

}
