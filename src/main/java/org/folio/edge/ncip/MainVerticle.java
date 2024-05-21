package org.folio.edge.ncip;

import org.folio.edge.core.EdgeVerticleHttp;
import org.folio.edge.core.utils.OkapiClientFactory;
import org.folio.edge.core.utils.OkapiClientFactoryInitializer;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.BodyHandler;


public class MainVerticle extends EdgeVerticleHttp {

	  @Override
	  public Router defineRoutes() {
      	    OkapiClientFactory ocf = OkapiClientFactoryInitializer.createInstance(vertx, config());
	    NcipHandler ncipHandler = new NcipHandler(secureStore, ocf);
	    Router router = Router.router(vertx);
	    router.route().handler(BodyHandler.create());
	    router.route(HttpMethod.POST, "/ncip/:apiKeyPath").handler(ncipHandler::handle);
	    router.route(HttpMethod.POST, "/ncip").handler(ncipHandler::handle);
	    router.route(HttpMethod.GET,"/ncipconfigcheck").handler(ncipHandler::handleConfigCheck);
	    router.route(HttpMethod.GET, "/nciphealthcheck").handler(ncipHandler::handleHealthCheck);
            router.route(HttpMethod.GET, "/admin/health").handler(this::handleHealthCheck);
	    return router;
	  }
}
