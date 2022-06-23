package org.folio.edge.ncip.utils;

import org.folio.edge.core.utils.OkapiClientFactory;

import io.vertx.core.Vertx;

public class NcipOkapiClientFactory extends OkapiClientFactory {

	  public NcipOkapiClientFactory(Vertx vertx, String okapiURL, int reqTimeoutMs) {
	    super(vertx, okapiURL, reqTimeoutMs);
	  }

	  public NcipOkapiClient getNcipOkapiClient(String tenant) {
	    return new NcipOkapiClient(vertx, okapiURL, tenant, reqTimeoutMs);
	  }
	}