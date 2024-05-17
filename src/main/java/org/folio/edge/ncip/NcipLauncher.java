package org.folio.edge.ncip;

import io.vertx.core.Launcher;

public class NcipLauncher extends Launcher {

  public static void main(String[] args) {

    new NcipLauncher().dispatch(args);
  }
}
