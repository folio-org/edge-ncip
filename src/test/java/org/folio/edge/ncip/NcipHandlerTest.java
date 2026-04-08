package org.folio.edge.ncip;

import static org.folio.edge.core.Constants.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class NcipHandlerTest {

    @Test
    public void requestBodyHelperReturnsBodyWhenPresent() throws Exception {
        NcipHandler handler = new NcipHandler(null, null);
        RoutingContext context = routingContextWithBody("payload");

        String body = invokeGetRequestBodyAsString(handler, context);

        assertEquals("payload", body);
    }

    @Test
    public void requestBodyHelperReturnsEmptyStringWhenBodyMissing() throws Exception {
        NcipHandler handler = new NcipHandler(null, null);
        RoutingContext context = routingContextWithBody(null);

        String body = invokeGetRequestBodyAsString(handler, context);

        assertEquals("", body);
    }

    @Test
    public void requestBodyHelperCanBeInvokedRepeatedly() throws Exception {
        NcipHandler handler = new NcipHandler(null, null);
        RoutingContext context = routingContextWithBody("config");

        assertEquals("config", invokeGetRequestBodyAsString(handler, context));
        assertEquals("config", invokeGetRequestBodyAsString(handler, context));
    }

    @Test
    public void handleProxyExceptionUsesRequestTimeoutForTimeouts() {
        ExceptionTrackingHandler handler = new ExceptionTrackingHandler();

        handler.handleProxyException(mock(RoutingContext.class), new TimeoutException("slow"));

        assertEquals("slow", handler.requestTimeoutMessage);
        assertNull(handler.internalServerErrorMessage);
    }

    @Test
    public void handleProxyExceptionUsesInternalServerErrorOtherwise() {
        ExceptionTrackingHandler handler = new ExceptionTrackingHandler();

        handler.handleProxyException(mock(RoutingContext.class), new IllegalStateException("broken"));

        assertEquals("broken", handler.internalServerErrorMessage);
        assertNull(handler.requestTimeoutMessage);
    }

    @Test
    public void internalServerErrorWritesStructuredJsonResponse() {
        NcipHandler handler = new NcipHandler(null, null);
        RoutingContext context = mock(RoutingContext.class);
        HttpServerResponse response = mock(HttpServerResponse.class);
        when(context.response()).thenReturn(response);
        when(response.ended()).thenReturn(false);
        when(response.setStatusCode(anyInt())).thenReturn(response);
        when(response.putHeader(any(CharSequence.class), anyString())).thenReturn(response);

        handler.internalServerError(context, "ignored");

        verify(response).setStatusCode(500);
        verify(response).putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).end(bodyCaptor.capture());
        assertNotNull(bodyCaptor.getValue());
        org.junit.Assert.assertTrue(bodyCaptor.getValue().contains("\"code\" : 500"));
        org.junit.Assert.assertTrue(bodyCaptor.getValue().contains("Internal Server Error"));
    }

    @Test
    public void internalServerErrorDoesNothingWhenResponseAlreadyEnded() {
        NcipHandler handler = new NcipHandler(null, null);
        RoutingContext context = mock(RoutingContext.class);
        HttpServerResponse response = mock(HttpServerResponse.class);
        when(context.response()).thenReturn(response);
        when(response.ended()).thenReturn(true);

        handler.internalServerError(context, "ignored");

        verify(response, never()).setStatusCode(anyInt());
        verify(response, never()).putHeader(any(CharSequence.class), anyString());
        verify(response, never()).end(anyString());
    }

    @Test
    public void handleProxyResponseCopiesStatusHeadersAndBody() {
        NcipHandler handler = new NcipHandler(null, null);
        RoutingContext context = mock(RoutingContext.class);
        HttpServerResponse response = mock(HttpServerResponse.class);
        HttpClientResponse clientResponse = mock(HttpClientResponse.class);

        when(context.response()).thenReturn(response);
        when(response.setStatusCode(anyInt())).thenReturn(response);
        when(response.putHeader(any(CharSequence.class), anyString())).thenReturn(response);
        when(clientResponse.statusCode()).thenReturn(200);
        when(clientResponse.getHeader(HttpHeaders.CONTENT_TYPE)).thenReturn("application/xml");

        doAnswer(invocation -> {
            io.vertx.core.Handler<Buffer> bodyHandler = invocation.getArgument(0);
            bodyHandler.handle(Buffer.buffer("ok"));
            return clientResponse;
        }).when(clientResponse).handler(any());

        doAnswer(invocation -> {
            io.vertx.core.Handler<Void> endHandler = invocation.getArgument(0);
            endHandler.handle(null);
            return clientResponse;
        }).when(clientResponse).endHandler(any());

        handler.handleProxyResponse(context, clientResponse);

        verify(response).setStatusCode(200);
        verify(response).putHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
        verify(response).end("ok");
    }

    @Test
    public void handleProxyResponseSkipsEmptyContentType() {
        NcipHandler handler = new NcipHandler(null, null);
        RoutingContext context = mock(RoutingContext.class);
        HttpServerResponse response = mock(HttpServerResponse.class);
        HttpClientResponse clientResponse = mock(HttpClientResponse.class);

        when(context.response()).thenReturn(response);
        when(response.setStatusCode(anyInt())).thenReturn(response);
        when(clientResponse.statusCode()).thenReturn(204);
        when(clientResponse.getHeader(HttpHeaders.CONTENT_TYPE)).thenReturn("");

        doAnswer(invocation -> {
            io.vertx.core.Handler<Buffer> bodyHandler = invocation.getArgument(0);
            bodyHandler.handle(Buffer.buffer(""));
            return clientResponse;
        }).when(clientResponse).handler(any());

        doAnswer(invocation -> {
            io.vertx.core.Handler<Void> endHandler = invocation.getArgument(0);
            endHandler.handle(null);
            return clientResponse;
        }).when(clientResponse).endHandler(any());

        handler.handleProxyResponse(context, clientResponse);

        verify(response).setStatusCode(204);
        verify(response, never()).putHeader(any(CharSequence.class), anyString());
        verify(response).end("");
    }

    private RoutingContext routingContextWithBody(String bodyValue) {
        RoutingContext context = mock(RoutingContext.class);
        HttpServerRequest request = mock(HttpServerRequest.class);
        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        headers.add("x-test", "value");

        when(context.request()).thenReturn(request);
        when(request.headers()).thenReturn(headers);

        if (bodyValue == null) {
            when(context.body()).thenReturn(null);
        } else {
            RequestBody body = mock(RequestBody.class);
            when(context.body()).thenReturn(body);
            when(body.asString()).thenReturn(bodyValue);
        }

        return context;
    }

    private String invokeGetRequestBodyAsString(NcipHandler handler, RoutingContext context) throws Exception {
        Method method = NcipHandler.class.getDeclaredMethod("getRequestBodyAsString", RoutingContext.class);
        method.setAccessible(true);
        return (String) method.invoke(handler, context);
    }

    private static class ExceptionTrackingHandler extends NcipHandler {
        private String requestTimeoutMessage;
        private String internalServerErrorMessage;

        ExceptionTrackingHandler() {
            super(null, null);
        }

        @Override
        protected void requestTimeout(RoutingContext ctx, String msg) {
            this.requestTimeoutMessage = msg;
        }

        @Override
        protected void internalServerError(RoutingContext ctx, String msg) {
            this.internalServerErrorMessage = msg;
        }
    }
}