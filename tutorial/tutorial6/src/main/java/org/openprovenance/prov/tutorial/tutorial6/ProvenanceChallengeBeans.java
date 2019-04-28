package org.openprovenance.prov.tutorial.tutorial6;

import java.util.Collection;

import com.sun.xml.bind.v2.schemagen.episode.Bindings;

public class ProvenanceChallengeBeans {


    public class AlignBindingBean {
        private String imgfile1;
        private String imglabel; 
        private String hdrfile1;
        private String hdrlabel;
        private String imgreffile1;
        private String imgreflabel; 
        private String hdrreffile1;
        private String hdrreflabel; 
        private String activity; 
        private String warpfile;
        private String warplabel;
        private String workflow;
        private String agent;


        public Collection<Bindings> export(Challenge<Collection<Bindings>> proxy) {
            return proxy.align(imgfile1, imglabel,
                               hdrfile1, hdrlabel,
                               imgreffile1, imgreflabel,
                               hdrreffile1, hdrreflabel,
                               activity,
                               warpfile, warplabel,
                               workflow, agent);
        }
        public void setImgFile(String imgfile1) {
            this.imgfile1=imgfile1;
        }
        public void setImgLabel(String imglabel) {
            this.imglabel=imglabel;
        }
        public void setHdrFile(String hdrfile1) {
            this.hdrfile1=hdrfile1;
        }
        public void setHdrLabel(String hdrlabel) {
            this.hdrlabel=hdrlabel;
        }
        public void setImgRefFile(String imgreffile1) {
            this.imgreffile1=imgreffile1;
        }
        public void setImgRefLabel(String imgreflabel) {
            this.imgreflabel=imgreflabel;
        }
        public void setHdrRefFile(String hdrreffile1) {
            this.hdrreffile1=hdrreffile1;
        }
        public void setHdrRefLabel(String hdrreflabel) {
            this.hdrreflabel=hdrreflabel;
        }
        public void setActivity(String activity) {
            this.activity=activity;
        }
        public void setWarpfile(String warpfile) {
            this.warpfile=warpfile;
        }
        public void setWarplabel(String warplabel) {
            this.warplabel=warplabel;
        }
        public void setWorkflow(String workflow) {
            this.workflow=workflow;
        }
        public void setAgent(String agent) {
            this.agent=agent;
        }
    }


}
