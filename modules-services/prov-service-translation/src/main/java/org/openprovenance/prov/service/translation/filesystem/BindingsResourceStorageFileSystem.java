package org.openprovenance.prov.service.translation.filesystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.storage.filesystem.NonDocumentGenericResourceStorageFileSystem;
import org.openprovenance.prov.template.json.Bindings;

import java.io.File;


public class BindingsResourceStorageFileSystem extends NonDocumentGenericResourceStorageFileSystem<Bindings> {
    public BindingsResourceStorageFileSystem(ObjectMapper om, File location) {
        super(om, Bindings.class, location);
    }
}
