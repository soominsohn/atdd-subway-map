package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.stations = null;
    }

    public Line(Long id, String name, String color) {
        this(name, color);
        this.id = id;
    }

    public Line(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Line(Line newLine, List<Station> stations) {
        this.id = newLine.getId();
        this.name = newLine.getName();
        this.color = newLine.getColor();
        this.stations = new ArrayList<>(stations);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }
}
