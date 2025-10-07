package org.openprovenance.prov.model.interop;

public class PrincipalManager {

    private static final ThreadLocal<String> principal = ThreadLocal.withInitial(() ->  "unknown");

    public void setPrincipal(String p) {
        principal.set(p);
    }

    public String getPrincipal() {
        return principal.get();
    }


}
