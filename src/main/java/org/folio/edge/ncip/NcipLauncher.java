package org.folio.edge.ncip;

import io.vertx.core.Launcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NcipLauncher extends Launcher {
  private static final Logger logger = LogManager.getLogger(NcipLauncher.class);

  public static void main(String[] args) {
    new NcipLauncher().dispatch(args);
  }
}
