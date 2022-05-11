package wooteco.subway.domain;

import java.util.List;

public class Section {

    private Long id;
    private Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public Section(Long lineId, Long upStationId, Long downStationId, int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(Long id, Long lineId, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(Long lineId, Section section) {
        this(section.getUpStationId(), section.getDownStationId(), section.getDistance());
        this.lineId = lineId;
    }

    public Section(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public boolean hasSameUpStation(Section section) {
        return this.upStationId.equals(section.upStationId);
    }

    public boolean hasSameDownStation(Section section) {
        return this.downStationId.equals(section.downStationId);
    }

    public boolean isSameUpStationId(Long upStationId) {
        return this.upStationId.equals(upStationId);
    }

    public boolean isSameDownStationId(Long downStationId) {
        return this.downStationId.equals(downStationId);
    }

    public boolean isGreaterOrEqualDistanceThan(Section section) {
        return this.distance >= section.distance;
    }

    public int calculateDistanceDifference(Section section) {
        return this.distance - section.distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }
}
