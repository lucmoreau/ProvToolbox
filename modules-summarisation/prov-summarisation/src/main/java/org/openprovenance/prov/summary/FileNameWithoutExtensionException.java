package org.openprovenance.prov.summary;


public class FileNameWithoutExtensionException extends RuntimeException {
  public FileNameWithoutExtensionException(String filename) {
    super(filename);
  }
}
