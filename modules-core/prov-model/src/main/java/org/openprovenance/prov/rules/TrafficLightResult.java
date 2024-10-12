package org.openprovenance.prov.rules;

public class TrafficLightResult implements Ansi {
    public String comment;
    public double ratio;
    public TrafficLight.TrafficLightColor color;
    public String colorAsString;

    public TrafficLightResult(String comment, double ratio, TrafficLight.TrafficLightColor color) {
        this.comment=comment;
        this.ratio=ratio;
        this.color=color;
        this.colorAsString=colorAsString(color);
    }

    public String colorAsString(TrafficLight.TrafficLightColor color) {
        return switch (color) {
            case RED -> red("red");
            case ORANGE -> orange("orange");
            case GREEN -> green("green");
            default -> throw new IllegalArgumentException("Unknown color: " + color);
        };
    }
}
