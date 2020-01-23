// Generated Automatically by ProvToolbox for templates config "templates"
package org.openprovenance.prov.storage.mongodb;

import java.lang.Exception;
import java.lang.String;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.Runner;

public class Init {
  public static final String[] builders;

  public static final ProvFactory pf;

  static {
    builders = new String[1];
    builders[0]="org.openprovenance.prov.storage.mongodb.Template_blockBuilder";
    pf= InteropFramework.getDefaultFactory();
  }

  public static boolean init() {
    return FileBuilder.registerBuilders(builders,pf);
  }

  public static void main(String[] args) throws Exception {
    init();
    Runner.main(args);
  }
}
