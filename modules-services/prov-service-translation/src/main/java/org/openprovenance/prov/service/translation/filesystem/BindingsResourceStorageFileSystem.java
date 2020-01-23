package org.openprovenance.prov.service.translation.filesystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.service.core.filesystem.NonDocumentGenericResourceStorageFileSystem;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;

import java.io.File;

import static org.openprovenance.prov.service.core.ServiceUtils.UPLOADED_FILE_PATH;

public class BindingsResourceStorageFileSystem extends NonDocumentGenericResourceStorageFileSystem<BindingsBean> {
    public BindingsResourceStorageFileSystem(ObjectMapper om) {
        super(om, BindingsBean.class, new File(UPLOADED_FILE_PATH));
    }
}
