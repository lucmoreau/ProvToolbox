package org.openprovenance.prov.service.translation.filesystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.service.core.filesystem.NonDocumentGenericResourceStorageFileSystem;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;

public class BindingsResourceStorageFileSystem extends NonDocumentGenericResourceStorageFileSystem<BindingsBean> {
    public BindingsResourceStorageFileSystem(ObjectMapper om) {
        super(om, BindingsBean.class);
    }
}
