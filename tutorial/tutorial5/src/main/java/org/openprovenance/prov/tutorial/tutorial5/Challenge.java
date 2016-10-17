package org.openprovenance.prov.tutorial.tutorial5;

import java.util.Collection;

import org.openprovenance.prov.model.StatementOrBundle;

public interface Challenge {

    public static final String PC1_NS = "http://www.ipaw.info/challenge/";
    public static final String PC1_PREFIX = "pc1";
    public static final String PRIM_NS = "http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX = "prim";

    public abstract Collection<StatementOrBundle> align(String imgfile,
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

    public abstract Collection<StatementOrBundle> reslice(String warp,
                                                          String activity,
                                                          String imgfile,
                                                          String imglabel,
                                                          String hdrfile,
                                                          String hdrlabel,
                                                          String workflow,
                                                          String agent);

    public abstract Collection<StatementOrBundle> softmean(String imgfile1,
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

    public abstract Collection<StatementOrBundle> slice(String imgatlas,
                                                        String hdratlas,
                                                        String params,
                                                        String paramslabel,
                                                        String paramsvalue,
                                                        String activity,
                                                        String pgmfile,
                                                        String pgmlabel,
                                                        String workflow,
                                                        String agent);

    public abstract Collection<StatementOrBundle> convert(String pgmfile,
                                                          String activity,
                                                          String giffile,
                                                          String giflabel,
                                                          String workflow,
                                                          String agent);

}