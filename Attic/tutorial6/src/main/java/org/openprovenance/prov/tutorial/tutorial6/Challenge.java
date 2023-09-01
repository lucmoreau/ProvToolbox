package org.openprovenance.prov.tutorial.tutorial6;

import java.util.Collection;



public interface Challenge<T> extends ChallengeConstants {

    public abstract T align(String imgfile,
                            String imglabel,
                            String hdrfile,
                            String hdrlabel,
                            String imgreffile1,
                            String imgreflabel,
                            String hdrreffile1,
                            String hdrreflabel,
                            String activity,
                            String warpfile,
                            String warplabel,
                            String workflow,
                            String agent);

    public abstract T reslice(String warp,
                              String activity,
                              String imgfile,
                              String imglabel,
                              String hdrfile,
                              String hdrlabel,
                              String workflow,
                              String agent);

    public abstract T softmean(String imgfile1,
                               String hdrfile1,
                               String imgfile2,
                               String hdrfile2,
                               String imgfile3,
                               String hdrfile3,
                               String imgfile4,
                               String hdrfile4,
                               String activity,
                               String imgatlas,
                               String imglabel,
                               String hdratlas,
                               String hdrlabel,
                               String workflow,
                               String agent);

    public abstract T slice(String imgatlas,
                            String hdratlas,
                            String params,
                            String paramslabel,
                            String paramsvalue,
                            String activity,
                            String pgmfile,
                            String pgmlabel,
                            String workflow,
                            String agent);

    public abstract  T convert(String pgmfile,
                               String activity,
                               String giffile,
                               String giflabel,
                               String workflow,
                               String agent);
    
    public abstract void overallWorkflow(Collection<T> ll);

    

}