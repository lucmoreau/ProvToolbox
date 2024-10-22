package org.openprovenance.prov.rules;

import java.util.LinkedList;
import java.util.List;



public class TrafficLightResult implements Ansi {
    public final ResultKind kind;
    public String comment;
    public double ratio;
    public TrafficLight.TrafficLightColor color;
    public String colorAsString;
    public List<TrafficLightResult> subResults=new LinkedList<>();
    public double weight=1.0;
    public String explanation;

    public enum ResultKind {
        PERCENTAGE, COMPOSITE, ABSOLUTE
    }

    public TrafficLightResult(String comment, double ratio, TrafficLight.TrafficLightColor color, String explanation) {
        this.comment=comment;
        this.ratio=ratio;
        this.kind=ResultKind.PERCENTAGE;
        if (ratio < 0 || ratio > 100.0) throw new IllegalArgumentException("Ratio out of range: " + ratio);
        this.color=color;
        this.colorAsString=colorAsString(color);
        this.explanation=explanation;
    }

    public TrafficLightResult(String comment, double ratio, ResultKind kind, TrafficLight.TrafficLightColor color, String explanation) {
        this.comment=comment;
        this.ratio=ratio;
        this.kind=kind;
        this.color=color;
        this.explanation=explanation;
        this.colorAsString=colorAsString(color);
    }


    public TrafficLightResult(String comment, List<TrafficLightResult> subResults, String explanation) {
        this.comment=comment;
        this.kind=ResultKind.COMPOSITE;
        double ratio=subResults.stream().map(r -> r.valueOf() * r.weight).reduce(0.0, Double::sum) * 1.0 / subResults.stream().map(r -> r.weight).reduce(0.0, Double::sum);
        this.ratio=ratio;
        if (ratio<0 || ratio>3) throw new IllegalArgumentException("Ratio out of range: " + ratio);
        this.color=colorOf(ratio);
        this.colorAsString=colorAsString(this.color);
        this.explanation=explanation;
        this.subResults.addAll(subResults);
    }

    public String colorAsString(TrafficLight.TrafficLightColor color) {
        return switch (color) {
            case RED -> red("red");
            case ORANGE -> orange("orange");
            case GREEN -> green("green");
        };
    }

    public int valueOf() {
        return switch (color) {
            case RED -> 1;
            case ORANGE -> 2;
            case GREEN -> 3;
        };
    }

    static public TrafficLight.TrafficLightColor colorOf(double ratio) {
        if (ratio >= 2.6) return TrafficLight.TrafficLightColor.GREEN;
        if (ratio < 1.7)  return TrafficLight.TrafficLightColor.RED;
        return TrafficLight.TrafficLightColor.ORANGE;
    }

}

