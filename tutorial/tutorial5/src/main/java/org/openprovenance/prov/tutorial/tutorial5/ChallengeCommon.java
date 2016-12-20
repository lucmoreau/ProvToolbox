package org.openprovenance.prov.tutorial.tutorial5;

import java.util.Collection;

import org.openprovenance.prov.model.ProvFactory;

abstract public class ChallengeCommon<T> extends ChallengeUtil implements Challenge<T> {
    
    public ChallengeCommon(ProvFactory pFactory) {
        super(pFactory);
    

    }

   
    
    public void overallWorkflow(Collection<T> ll) {
        ll.add(align("anatomy1.img", "Anatomy I1", "anatomy1.hdr", "Anatomy H1", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp1","warp1.warp", "Warp Params1", "a#pcworkflow","ag1"));
        ll.add(align("anatomy2.img", "Anatomy I2", "anatomy2.hdr", "Anatomy H2", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp2","warp2.warp", "Warp Params2", "a#pcworkflow","ag1"));
        ll.add(align("anatomy3.img", "Anatomy I3", "anatomy3.hdr", "Anatomy H3", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp3","warp3.warp", "Warp Params3", "a#pcworkflow","ag1"));
        ll.add(align("anatomy4.img", "Anatomy I4", "anatomy4.hdr", "Anatomy H4", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp4","warp4.warp", "Warp Params4", "a#pcworkflow","ag1"));


        ll.add(reslice("warp1.warp", "a#reslice1", "resliced1.img", "Resliced I1", "resliced1.hdr", "Resliced H1", "a#pcworkflow","ag1"));
        ll.add(reslice("warp2.warp", "a#reslice2", "resliced2.img", "Resliced I2", "resliced2.hdr", "Resliced H2", "a#pcworkflow","ag1"));
        ll.add(reslice("warp3.warp", "a#reslice3", "resliced3.img", "Resliced I3", "resliced3.hdr", "Resliced H3", "a#pcworkflow","ag1"));
        ll.add(reslice("warp4.warp", "a#reslice4", "resliced4.img", "Resliced I4", "resliced4.hdr", "Resliced H4", "a#pcworkflow","ag1"));


        ll.add(softmean("resliced1.img", "resliced1.hdr", "resliced2.img", "resliced2.hdr", "resliced3.img", "resliced3.hdr", "resliced4.img", "resliced4.hdr", "a#softmean", "atlas.img", "Atlas Image", "atlas.hdr", "Atlas Header", "a#pcworkflow","ag1"));


        ll.add(slice("atlas.img", "atlas.hdr",  "params#slicer1", "slicer param 1", "-x .5", "a#slicer1", "atlas-x.pgm", "Atlas X Slice", "a#pcworkflow","ag1"));
        ll.add(slice("atlas.img", "atlas.hdr",  "params#slicer2", "slicer param 2", "-y .5", "a#slicer2", "atlas-y.pgm", "Atlas Y Slice", "a#pcworkflow","ag1"));
        ll.add(slice("atlas.img", "atlas.hdr",  "params#slicer3", "slicer param 3", "-z .5", "a#slicer3", "atlas-z.pgm", "Atlas Z Slice", "a#pcworkflow","ag1"));

        
        ll.add(convert("atlas-x.pgm", "a#convert1", "atlas-x.gif", "Atlas X Graphic", "a#pcworkflow","ag1"));
        ll.add(convert("atlas-y.pgm", "a#convert2", "atlas-y.gif", "Atlas Y Graphic", "a#pcworkflow","ag1"));
        ll.add(convert("atlas-z.pgm", "a#convert3", "atlas-z.gif", "Atlas Z Graphic", "a#pcworkflow","ag1"));
        
    }
}
