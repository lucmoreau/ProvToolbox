package org.openprovenance.prov.service.xplain;

import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

public class NarrativeServiceUtils extends ServiceUtils {

    public static final String KEY_EXPLANATIONS = "explanations";
    public static final String KEY_MAP = "keymap";
    public static final String DETAILS = "details";
    public static final String KEY_TEMPLATES = "templates";
    public static final String KEY_PROFILES = "profiles";


    public NarrativeServiceUtils(PostService ps, ServiceUtilsConfig config) {
        super(ps,config);
    }

    Map<String,Object> getExtension(ResourceIndex<DocumentResource> index, String id, DocumentResource dr) {
        Object result=dr.getExtension().get(KEY_EXPLANATIONS);
        if (result==null) {
            result=new HashMap<String,Object>();
            dr.getExtension().put(KEY_EXPLANATIONS,result);
            dr.setExtension(dr.getExtension());
            index.put(id,dr);
        }
        return (Map<String,Object>) result;
    }
    Map<String,Integer> getKeymap(ResourceIndex<DocumentResource> index, String id, DocumentResource dr) {
        Map<String, Object> extension=getExtension(index,id,dr);
        Object result=extension.get(KEY_MAP);
        if (result==null) {
            result=new HashMap<String,Integer>();
            extension.put(KEY_MAP,result);
            dr.setExtension(dr.getExtension());
            index.put(id,dr);
        }
        return (Map<String,Integer>) result;

    }
    Map<String,Map<String,String>> getDetails(ResourceIndex<DocumentResource> index, String id, DocumentResource dr) {
        Map<String, Object> extension=getExtension(index, id,dr);
        Object result=extension.get(DETAILS);
        if (result==null) {
            result=new HashMap<String,String>();
            extension.put(DETAILS,result);
            dr.setExtension(dr.getExtension());
            index.put(id,dr);
        }
        return (Map<String,Map<String,String>>) result;
    }

    public String createEntryForTemplateAndProfile(ResourceIndex<DocumentResource> index, String id, DocumentResource dr, String templates_str, String profiles_str) {
        String key="" + templates_str + "|" + profiles_str;

        String keyId=null;
        Map<String, Integer> map=getKeymap(index,id,dr);
        if (map.containsKey(key)) {
            keyId=map.get(key).toString();
        } else {
            int size=map.size();
            map.put(key, size);
            keyId="" +size;
        }

        Map<String, Map<String, String>> details=getDetails(index,id,dr);
        Map<String,String> entry= new HashMap<>();
        entry.put(NarrativeServiceUtils.KEY_TEMPLATES, templates_str);
        entry.put(NarrativeServiceUtils.KEY_PROFILES,  profiles_str);
        details.put(keyId,entry);
        dr.setExtension(dr.getExtension());
        return keyId;
    }


    public Response.ResponseBuilder composeResponseCreated(Object o, String path, UriInfo uriInfo) {
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(path);
        return Response.created(builder.build()).entity(o).header("Access-Control-Allow-Origin", "*");
    }





}
