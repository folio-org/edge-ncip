package org.folio.edge.ncip;


import static org.folio.edge.core.Constants.APPLICATION_JSON;
import static org.folio.edge.core.Constants.APPLICATION_XML;
import static org.folio.edge.core.Constants.MSG_INTERNAL_SERVER_ERROR;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

import org.folio.edge.core.utils.test.MockOkapi;


import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class NcipMockOkapi extends MockOkapi{
	
	  private static final Logger logger = LogManager.getLogger(NcipMockOkapi.class);
	
	  public NcipMockOkapi(int port, List<String> knownTenants) {
		    super(port, knownTenants);
	  }
	  
	  
	  @Override
	  public Router defineRoutes() {
	    Router router = super.defineRoutes();
	    router.route(HttpMethod.POST, "/ncip").handler(this::ncipHandler);
	    router.route(HttpMethod.POST, "/ncip/apikey").handler(this::ncipHandler);
	    router.route(HttpMethod.GET, "/nciphealthcheck").handler(this::ncipHealthCheck);
	    router.route(HttpMethod.GET, "/ncipconfigcheck").handler(this::ncipconfigcheck);
	    return router;
	  }
	  
	  
	  public void ncipHandler(RoutingContext ctx) {
		logger.info("called ncip handler");
		ctx.response()
        .setStatusCode(200)
        .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_XML)
        .end("ok");
	  }
	  
	  public void ncipHealthCheck(RoutingContext ctx) {
		logger.info("called ncip health check");
		ctx.response()
        .setStatusCode(200)
        .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_XML)
        .end("ok");
	  }
	  
	  public void ncipconfigcheck(RoutingContext ctx) {
		logger.info("called ncip config check");
		ctx.response()
        .setStatusCode(200)
        .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_XML)
        .end("ok");
	  }
	  

	    
	    

}
