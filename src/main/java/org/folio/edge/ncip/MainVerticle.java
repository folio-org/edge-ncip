package org.folio.edge.ncip;

import org.folio.edge.core.EdgeVerticleHttp;
import org.folio.edge.core.utils.OkapiClientFactory;
import org.folio.edge.core.utils.OkapiClientFactoryInitializer;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.BodyHandler;
import static org.folio.edge.core.Constants.SYS_OKAPI_URL;
import static org.folio.edge.core.Constants.SYS_REQUEST_TIMEOUT_MS;


public class MainVerticle extends EdgeVerticleHttp {

	  final private String okapiUrl = System.getProperty(SYS_OKAPI_URL);
	  private int reqTimeoutMs;


	  public MainVerticle() {
	    super();
	    if (System.getProperty(SYS_REQUEST_TIMEOUT_MS) != null) {
	        reqTimeoutMs = Integer.parseInt(System.getProperty(SYS_REQUEST_TIMEOUT_MS));
	      } else {
	        reqTimeoutMs = 35000;
	      }
	  }

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
