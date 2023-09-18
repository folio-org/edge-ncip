package org.folio.edge.ncip;


import static org.folio.edge.core.Constants.SYS_LOG_LEVEL;
import static org.folio.edge.core.Constants.SYS_OKAPI_URL;
import static org.folio.edge.core.Constants.SYS_PORT;
import static org.folio.edge.core.Constants.SYS_REQUEST_TIMEOUT_MS;
import static org.folio.edge.core.Constants.SYS_SECURE_STORE_PROP_FILE;
import static org.folio.edge.core.Constants.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.spy;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.edge.core.utils.ApiKeyUtils;
import org.folio.edge.core.utils.test.TestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;


@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

  private static final Logger logger = LogManager.getLogger(MainVerticleTest.class);

  protected static final String titleId = "0c8e8ac5-6bcc-461e-a8d3-4b55a96addc8";
  protected static final String apiKey = ApiKeyUtils.generateApiKey(10, "diku", "diku");
  private static final String badApiKey = apiKey + "0000";
  private static final String unknownTenantApiKey = ApiKeyUtils.generateApiKey(10, "bogus", "diku");

  private static final int requestTimeoutMs = 30000;

  private static Vertx vertx;
  protected static NcipMockOkapi mockOkapi;

  @BeforeClass
  public static void setUpOnce(TestContext context) throws Exception {
    int okapiPort = TestUtils.getPort();
    int serverPort = TestUtils.getPort();

    List<String> knownTenants = new ArrayList<>();
    knownTenants.add(ApiKeyUtils.parseApiKey(apiKey).tenantId);

    mockOkapi = spy(new NcipMockOkapi(okapiPort, knownTenants));
    mockOkapi.start().onComplete(context.asyncAssertSuccess());

    vertx = Vertx.vertx();

    System.setProperty(SYS_PORT, String.valueOf(serverPort));
    System.setProperty(SYS_OKAPI_URL, "http://localhost:" + okapiPort);
    System.setProperty(SYS_SECURE_STORE_PROP_FILE, "src/main/resources/ephemeral.properties");
    System.setProperty(SYS_LOG_LEVEL, "DEBUG");
    System.setProperty(SYS_REQUEST_TIMEOUT_MS, String.valueOf(requestTimeoutMs));

    final DeploymentOptions opt = new DeploymentOptions();
    vertx.deployVerticle(MainVerticle.class.getName(), opt, context.asyncAssertSuccess());

    RestAssured.baseURI = "http://localhost:" + serverPort;
    RestAssured.port = serverPort;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @AfterClass
  public static void tearDownOnce(TestContext context) {
    logger.info("Shutting down server");
    mockOkapi.close().onComplete(context.asyncAssertSuccess());
  }


  @Test
  public void testAdminHealthShouldSucceed(TestContext context) {
    logger.info("=== Test the health check endpoint ===");

    final Response resp = RestAssured
      .get("/admin/health")
      .then()
      .contentType(TEXT_PLAIN)
      .statusCode(200)
      .header(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN)
      .extract()
      .response();

    assertEquals("\"OK\"", resp.body().asString());
  }
  
  @Test
  public void failsWhenBadApiKeyProvided(TestContext context) throws Exception {
    logger.info("=== Test request with malformed apiKey ===");

    final Response resp = RestAssured
      .post(String.format("/ncip/"+ badApiKey))
      .then()
      .statusCode(401)
      .extract()
      .response();
    ErrorMessage error = new ErrorMessage(resp.getStatusCode(), resp.asPrettyString());
    logger.info(error.toXml());
    logger.info(error.toJson());
    logger.info(resp.body().asString());

  }
  
  @Test
  public void successWithGoodKey(TestContext context) throws Exception {
    logger.info("=== Test request with good apiKey ===");

    final Response resp = RestAssured
      .given()
      .post(String.format("/ncip/"+apiKey))
      .then()
      .statusCode(200)
      .extract()
      .response();
    logger.info(resp.body().asString());

  }
  
  @Test
  public void testNciphealthcheck(TestContext context) throws Exception {
	    logger.info("=== Test the health check 2 endpoint ===");

	    final Response resp = RestAssured
	      .get("/nciphealthcheck?apiKey=" + apiKey)
	      .then()
	      .statusCode(200)
	      .extract()
	      .response();

  }
  
  @Test
  public void testConfigCheck(TestContext context) throws Exception {
	  logger.info("=== Test ncipconfigcheck ===");
	  final Response resp = RestAssured
		      .get("/ncipconfigcheck?apiKey=" + apiKey)
		      .then()
		      .statusCode(200)
		      .extract()
		      .response();
	  logger.info(resp.asPrettyString());
  }
  
  @Test
  public void testErrorMessage() {
	  ErrorMessage errorMessage = new ErrorMessage(200,"ok");
	  assertFalse(errorMessage.equals("ok"));
  }
  
  @Test
  public void testErrorMessagesEquality() {
	  ErrorMessage errorMessageOrig = new ErrorMessage(500,"bad request");
	  ErrorMessage errorMessage = new ErrorMessage(200,"ok");
	  assertFalse(errorMessage.equals("ok"));
	  assertFalse(errorMessage.equals(errorMessageOrig));
  }
  
  @Test
  public void testErrorCodeEquality() {
	  ErrorMessage errorMessageOrig = new ErrorMessage(200,"bad request");
	  ErrorMessage errorMessage = new ErrorMessage(200,"ok");
	  assertFalse(errorMessage.equals("ok"));
	  assertFalse(errorMessage.equals(errorMessageOrig));
  }
  
  @Test
  public void testErrorMessageEqualityWithNull() {
	  ErrorMessage errorMessageOrig = new ErrorMessage(200,null);
	  ErrorMessage errorMessageEasy = ErrorMessage.builder().chargeAmount("bad request").item(200).build();
	  ErrorMessage errorMessage = new ErrorMessage(200,"ok");
	  assertFalse(errorMessageOrig.equals(errorMessage));
	  assertFalse(errorMessage.equals("ok"));
	  assertFalse(errorMessage.equals(errorMessageOrig));
  }
  



 
}